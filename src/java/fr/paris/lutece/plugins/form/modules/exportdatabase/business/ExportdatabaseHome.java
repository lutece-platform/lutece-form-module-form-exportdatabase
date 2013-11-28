/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.form.modules.exportdatabase.business;

import fr.paris.lutece.plugins.form.business.EntryHome;
import fr.paris.lutece.plugins.form.business.Field;
import fr.paris.lutece.plugins.form.business.FieldHome;
import fr.paris.lutece.plugins.form.business.Form;
import fr.paris.lutece.plugins.form.business.FormHome;
import fr.paris.lutece.plugins.form.business.FormSubmit;
import fr.paris.lutece.plugins.form.business.FormSubmitHome;
import fr.paris.lutece.plugins.form.business.IEntry;
import fr.paris.lutece.plugins.form.business.Response;
import fr.paris.lutece.plugins.form.business.ResponseFilter;
import fr.paris.lutece.plugins.form.business.ResponseHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


/**
 * 
 * @author lyaete
 * 
 */
public class ExportdatabaseHome
{
    public static final int ENTRY_TYPE_FILE_IDENTIFIER = 8;

    // Static variable pointed at the DAO instance
    private static final String EMPTY_STRING = "";
    private static final String PROPERTY_MULTIPLE_RESPONSE_SEPARATOR = "form-exportdatabase.multiple_response_separator";

    private static IExportdatabaseDAO _dao = SpringContextService.getBean( "form-exportdatabase.exportdatabaseDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ExportdatabaseHome( )
    {
    }

    /**
     * Check if the table exists
     * 
     * @param strTableName The table name
     * @param plugin the plugin
     * @return true if the table exists or false else
     */
    public static boolean tableExists( String strTableName, Plugin plugin )
    {
        return _dao.tableExists( strTableName, plugin );
    }

    /**
     * Create the tables
     * 
     * @param formConfiguration the {@link FormConfiguration}
     * @param pluginForm The form plugin
     * @param pluginExportDatabase The export database plugin
     */
    public static void createTables( FormConfiguration formConfiguration, Plugin pluginForm, Plugin pluginExportDatabase )
    {
        Form form = FormHome.findByPrimaryKey( formConfiguration.getIdForm( ), pluginForm );
        // Create references tables
        createReferencesTables( formConfiguration, form, pluginForm, pluginExportDatabase );
        _dao.createTables( formConfiguration, pluginExportDatabase );
    }

    /**
     * Drop the export tables
     * 
     * @param formConfiguration The {@link FormConfiguration}
     * @param pluginForm The form plugin
     * @param pluginExportDatabase The export database plugin
     */
    public static void dropTables( FormConfiguration formConfiguration, Plugin pluginForm, Plugin pluginExportDatabase )
    {
        _dao.dropTable( formConfiguration.getTableName( ), pluginExportDatabase );
        _dao.dropTable( formConfiguration.getTableNameBlob( ), pluginExportDatabase );

        Form form = FormHome.findByPrimaryKey( formConfiguration.getIdForm( ), pluginForm );
        dropReferencesTables( formConfiguration, form, pluginForm, pluginExportDatabase );
    }

    /**
     * Export all formSubmit for Form
     * 
     * @param form the {@link Form}
     * @param pluginForm The form plugin
     * @param pluginExportDatabase The export database plugin
     */
    public static void exportAllFormSubmit( Form form, Plugin pluginForm, Plugin pluginExportDatabase )
    {
        ResponseFilter filter = new ResponseFilter( );
        filter.setIdForm( form.getIdForm( ) );

        List<FormSubmit> formSubmitList = FormSubmitHome.getFormSubmitList( filter, pluginForm );

        for ( FormSubmit formSubmit : formSubmitList )
        {
            addFormSubmit( formSubmit, pluginForm, pluginExportDatabase );
        }
    }

    /**
     * Get the number of submitted forms in the export table
     * 
     * @param formConfiguration The {@link FormConfiguration}
     * @param plugin The {@link Plugin}
     * @return the number of submitted forms
     */
    public static int countFormSubmitted( FormConfiguration formConfiguration, Plugin plugin )
    {
        return _dao.countFormSubmit( formConfiguration, plugin );
    }

    /**
     * Add records corresponding to submitted forms
     * 
     * @param formSubmit The submitted form
     * @param pluginForm The form plugin
     * @param pluginExportDatabase The export database plugin
     */
    public static void addFormSubmit( FormSubmit formSubmit, Plugin pluginForm, Plugin pluginExportDatabase )
    {
        ReferenceList listResponses = new ReferenceList( );
        FormConfiguration formConfiguration = FormConfigurationHome.findByPrimaryKey(
                formSubmit.getForm( ).getIdForm( ), pluginExportDatabase );
        Collection<EntryConfiguration> entryConfigurationList = formConfiguration
                .getEntryConfigurationList( pluginExportDatabase );
        ResponseFilter filter = new ResponseFilter( );
        filter.setIdForm( formSubmit.getIdFormSubmit( ) );
        HashMap<Integer, List<Response>> mapEntryListResponses = new HashMap<Integer, List<Response>>( );
        Response responseTemp;
        for ( Response response : ResponseHome.getResponseList( filter ) )
        {
            if ( !mapEntryListResponses.containsKey( response.getEntry( ).getIdEntry( ) ) )
            {
                mapEntryListResponses.put( response.getEntry( ).getIdEntry( ), new ArrayList<Response>( ) );
            }
            mapEntryListResponses.get( response.getEntry( ).getIdEntry( ) ).add( response );

        }
        for ( EntryConfiguration entryConfiguration : entryConfigurationList )
        {
            if ( mapEntryListResponses.containsKey( entryConfiguration.getIdEntry( ) ) )
            {

                IEntry entry = EntryHome.findByPrimaryKey( entryConfiguration.getIdEntry( ) );

                if ( entry.getEntryType( ).getIdType( ) == ENTRY_TYPE_FILE_IDENTIFIER )
                {

                    responseTemp = mapEntryListResponses.get( entryConfiguration.getIdEntry( ) ).get( 0 );
                    _dao.createFileToTable( formConfiguration.getTableNameBlob( ), formSubmit.getIdFormSubmit( ),
                            entryConfiguration.getColumnName( ), responseTemp.getFile( ) != null ? responseTemp
                                    .getFile( ).getTitle( ) : null, responseTemp.getValueResponse( ),
                            pluginExportDatabase );
                }
                else
                {
                    listResponses.add( setEntryColumn( entryConfiguration,
                            mapEntryListResponses.get( entryConfiguration.getIdEntry( ) ) ) );
                }
            }
        }

        _dao.createRecordToTable( formSubmit.getIdFormSubmit( ), formSubmit.getDateResponse( ), formSubmit.getIp( ),
                formConfiguration.getTableName( ), listResponses, pluginExportDatabase );
    }

    /**
     * Create the reference tables list
     * @param formConfiguration The {@link FormConfiguration}
     * @param form The {@link Form}
     * @param pluginForm The {@link Plugin} object for plugin Form
     * @param pluginExportdatabase The {@link Plugin} object for module form
     *            exportdatabase
     */
    public static void createReferencesTables( FormConfiguration formConfiguration, Form form, Plugin pluginForm,
            Plugin pluginExportdatabase )
    {
        for ( EntryConfiguration entry : formConfiguration.getEntryTextConfigurationList( pluginExportdatabase ) )
        {
            if ( entry.hasReferenceTable( ) )
            {
                // Create reference table
                ReferenceList referenceList = new ReferenceList( );

                for ( Field field : FieldHome.getFieldListByIdEntry( entry.getIdEntry( ) ) )
                {
                    referenceList.addItem( field.getValue( ), field.getTitle( ) );
                }

                _dao.createReferenceTable( formConfiguration.getTableName( ), entry.getColumnName( ), referenceList,
                        pluginExportdatabase );
            }
        }
    }

    /**
     * Drop the reference tables list
     * @param formConfiguration The {@link FormConfiguration}
     * @param form The {@link Form}
     * @param pluginForm The {@link Plugin} object for plugin Form
     * @param pluginExportdatabase The {@link Plugin} object for module form
     *            exportdatabase
     */
    private static void dropReferencesTables( FormConfiguration formConfiguration, Form form, Plugin pluginForm,
            Plugin pluginExportdatabase )
    {
        for ( EntryConfiguration entry : formConfiguration.getEntryTextConfigurationList( pluginExportdatabase ) )
        {
            if ( entry.hasReferenceTable( ) )
            {
                // Drop reference table
                _dao.dropReferenceTable( formConfiguration.getTableName( ), entry.getColumnName( ),
                        pluginExportdatabase );
            }
        }
    }

    /**
     * Set the entry value into a referenceItem to be inserted into table
     * 
     * @param entryConfiguration The {@link EntryConfiguration}
     * @param listResponse the list of response associate to the question
     * @return The {@link ReferenceItem}
     */
    private static ReferenceItem setEntryColumn( EntryConfiguration entryConfiguration, List<Response> listResponse )
    {
        ReferenceItem item = new ReferenceItem( );
        item.setCode( entryConfiguration.getColumnName( ) );

        String strValue = EMPTY_STRING;
        int nCpt = 0;
        for ( Response response : listResponse )
        {
            nCpt++;
            if ( response.getResponseValue( ) != null )
            {
                strValue += response.getEntry( ).getResponseValueForExport( null, response,
                        I18nService.getDefaultLocale( ) );
            }
            if ( nCpt < listResponse.size( ) )
            {
                strValue += AppPropertiesService.getProperty( PROPERTY_MULTIPLE_RESPONSE_SEPARATOR, "," );
            }
        }
        item.setName( strValue );

        return item;
    }
}
