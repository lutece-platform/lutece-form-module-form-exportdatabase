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

import fr.paris.lutece.plugins.form.business.Form;
import fr.paris.lutece.plugins.form.business.FormHome;
import fr.paris.lutece.plugins.form.business.FormSubmit;
import fr.paris.lutece.plugins.form.business.FormSubmitHome;
import fr.paris.lutece.plugins.form.business.IEntry;
import fr.paris.lutece.plugins.form.business.ResponseFilter;
import fr.paris.lutece.plugins.form.business.outputprocessor.OutputProcessor;
import fr.paris.lutece.plugins.form.modules.exportdatabase.service.ExportdatabasePlugin;
import fr.paris.lutece.plugins.form.utils.FormUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 * @author ELY
 * 
 */
public class ProcessorExportdatabase extends OutputProcessor
{
    // Templates
    private static final String TEMPLATE_CONFIGURATION_EXPORTDATABASE = "admin/plugins/form/modules/exportdatabase/processorexportdatabase/configuration_exportdatabase.html";

    // Parameters
    private static final String PARAMETER_ID_FORM = "id_form";
    private static final String PARAMETER_ID_TABLE_NAME = "table_name";
    private static final String PARAMETER_ID_COLUMN_NAME_PREFIX = "column_name_";
    private static final String PARAMETER_EXPORT_ALL = "export_all";
    private static final String PARAMETER_ACTION_EXPORTDATABASE = "action_exportdatabase";

    // Properties
    private static final String PROPERTY_SUFFIX_TABLE_NAME_BLOB = "form-exportdatabase.table_name_blob.suffix";
    private static final String PROPERTY_PREFIX_AUTO_FILL = "form-exportdatabase.auto_fill.prefix";

    // Markers
    private static final String MARK_FORM = "form";
    private static final String MARK_REF_LIST_ENTRY = "ref_list_entry";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_FORM_CONFIGURATION = "form_configuration";
    private static final String MARK_ENTRY_CONFIGURATION_LIST = "entry_configuration_list";
    private static final String MARK_FORM_SUBMIT = "form_submit";
    private static final String MARK_FORM_EXPORTED = "form_exported";
    private static final String MARK_AUTO_FILL = "auto_fill";

    // I18n messages
    private static final String MESSAGE_ERROR_TABLE_NAME = "module.form.exportdatabase.configuration_exportdatabase.message.errorTableName";
    private static final String MESSAGE_TABLE_NAME_ALREADY_EXISTS = "module.form.exportdatabase.configuration_exportdatabase.message.tableNameAlreadyExists";
    private static final String MESSAGE_ERROR_COLUMN_NAME = "module.form.exportdatabase.configuration_exportdatabase.message.errorColumnName";

    // Miscellaneous
    private static final String REGEX_ID = "^[\\d]+$";
    private static final String REGEX_SQL = "^[a-zA-Z_]{1}[a-zA-Z0-9_@$#]*$";
    private static final String EMPTY_STRING = "";
    private static final String EXPORT_ALL_YES = "yes";
    private static final String DEFAULT_SUFFIX_TABLE_NAME_BLOB = "_blob";
    private static final String DEFAULT_PREFIX_AUTO_FILL = "column";
    private static final String ACTION_SET_CONFIGURATION = "set_config";
    private static final String ACTION_DELETE_TABLES = "delete_exporttables";
    private static final String ACTION_DELETE_CONFIGURATION = "delete_configuration";
    private static final String ACTION_DELETE_AND_EXPORT_ALL = "delete_exportall";
    private static int[] arrayEntryTypeLong = new int[] { 3, 7 };
    private static int[] arrayEntryTypeRef = new int[] { 1, 2, 5 };

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.paris.lutece.plugins.form.business.outputprocessor.IOutputProcessor
     * #getOutputConfigForm(fr.paris.lutece.plugins.form.business.Form,
     * java.util.Locale, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public String getOutputConfigForm( HttpServletRequest request, Form form, Locale locale, Plugin plugin )
    {
        Plugin pluginExportdatabase = PluginService.getPlugin( ExportdatabasePlugin.PLUGIN_NAME );
        Map<String, Object> model = new HashMap<String, Object>( );
        FormConfiguration formConfiguration = FormConfigurationHome.findByPrimaryKey( form.getIdForm( ),
                pluginExportdatabase );

        if ( formConfiguration == null )
        {
            formConfiguration = new FormConfiguration( );
            formConfiguration.setIdForm( form.getIdForm( ) );
        }

        Collection<EntryConfiguration> entryConfigurationList = new ArrayList<EntryConfiguration>( );

        for ( IEntry entry : FormUtils.getAllQuestionList( form.getIdForm( ), plugin ) )
        {
            EntryConfiguration entryConfigurationFromEntry = EntryConfigurationHome.findByPrimaryKey(
                    form.getIdForm( ), entry.getIdEntry( ), pluginExportdatabase );

            if ( entryConfigurationFromEntry == null )
            {
                entryConfigurationFromEntry = new EntryConfiguration( );
                entryConfigurationFromEntry.setIdEntry( entry.getIdEntry( ) );
                entryConfigurationFromEntry.setIdForm( form.getIdForm( ) );

                //entryConfigurationFromEntry.setColumnName( "c" + entry.getIdEntry(  ) ); //Only for dev
            }

            entryConfigurationList.add( entryConfigurationFromEntry );
        }

        model.put( MARK_FORM, form );
        model.put( MARK_LOCALE, locale );
        model.put( MARK_FORM_CONFIGURATION, formConfiguration );
        model.put( MARK_ENTRY_CONFIGURATION_LIST, entryConfigurationList );

        int nFormSubmit = 0;

        if ( ExportdatabaseHome.tableExists( formConfiguration.getTableName( ), pluginExportdatabase ) )
        {
            nFormSubmit = ExportdatabaseHome.countFormSubmitted( formConfiguration, pluginExportdatabase );
        }

        model.put( MARK_FORM_EXPORTED, nFormSubmit );
        model.put( MARK_AUTO_FILL,
                AppPropertiesService.getProperty( PROPERTY_PREFIX_AUTO_FILL, DEFAULT_PREFIX_AUTO_FILL ) );

        ResponseFilter filter = new ResponseFilter( );
        filter.setIdForm( form.getIdForm( ) );

        int nCountFormSubmit = FormSubmitHome.getCountFormSubmit( filter, plugin );
        model.put( MARK_FORM_SUBMIT, nCountFormSubmit );
        model.put( MARK_REF_LIST_ENTRY, FormUtils.getRefListAllQuestions( form.getIdForm( ), plugin ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONFIGURATION_EXPORTDATABASE, locale, model );

        return template.getHtml( );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.paris.lutece.plugins.form.business.outputprocessor.IOutputProcessor
     * #doOutputConfigForm(javax.servlet.http.HttpServletRequest,
     * java.util.Locale, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public String doOutputConfigForm( HttpServletRequest request, Locale locale, Plugin plugin )
    {
        Plugin pluginExportdatabase = PluginService.getPlugin( ExportdatabasePlugin.PLUGIN_NAME );
        String strIdForm = request.getParameter( PARAMETER_ID_FORM );

        String strActionExportdatabase = request.getParameter( PARAMETER_ACTION_EXPORTDATABASE );

        if ( ( strIdForm == null ) || !strIdForm.matches( REGEX_ID ) )
        {
            return Messages.MANDATORY_FIELDS;
        }

        Form form = FormHome.findByPrimaryKey( Integer.parseInt( strIdForm ), plugin );

        if ( form == null )
        {
            return Messages.MANDATORY_FIELDS;
        }

        if ( ( strActionExportdatabase == null ) )
        {
            return null;
        }
        else if ( strActionExportdatabase.equals( ACTION_SET_CONFIGURATION ) )
        {
            return doActionSetConfiguration( request, form, plugin, pluginExportdatabase );
        }
        else if ( strActionExportdatabase.equals( ACTION_DELETE_CONFIGURATION ) )
        {
            doActionDeleteTables( request, form, plugin, pluginExportdatabase );

            return doActionDeleteConfiguration( request, form, plugin, pluginExportdatabase );
        }
        else if ( strActionExportdatabase.equals( ACTION_DELETE_TABLES ) )
        {
            return doActionDeleteTables( request, form, plugin, pluginExportdatabase );
        }
        else if ( strActionExportdatabase.equals( ACTION_DELETE_AND_EXPORT_ALL ) )
        {
            return doActionDeleteTablesAndExportAllFormSubmit( request, form, plugin, pluginExportdatabase );
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.paris.lutece.plugins.form.business.outputprocessor.IOutputProcessor
     * #process(fr.paris.lutece.plugins.form.business.FormSubmit,
     * javax.servlet.http.HttpServletRequest,
     * fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public String process( FormSubmit formSubmit, HttpServletRequest request, Plugin plugin )
    {
        Plugin pluginExportdatabase = PluginService.getPlugin( ExportdatabasePlugin.PLUGIN_NAME );
        FormConfiguration formConfiguration = FormConfigurationHome.findByPrimaryKey(
                formSubmit.getForm( ).getIdForm( ), pluginExportdatabase );

        if ( formConfiguration == null )
        {
            AppLogService.error( "Error exportdatabase when retrieving form configuration." );

            return null; //FIXME
        }

        if ( !ExportdatabaseHome.tableExists( formConfiguration.getTableName( ), pluginExportdatabase )
                || !ExportdatabaseHome.tableExists( formConfiguration.getTableNameBlob( ), pluginExportdatabase ) )
        {
            AppLogService.error( "Error exportdatabase : table does not exists !" );

            return null; //FIXME
        }

        ExportdatabaseHome.addFormSubmit( formSubmit, plugin, pluginExportdatabase );

        return null; //No error
    }

    /**
     * Process the configuration settings
     * 
     * @param request The {@link HttpServletRequest}
     * @param form The {@link Form} linked to this outputProcessor
     * @param plugin The {@link Plugin}
     * @return An error message key or null if no error
     */
    private String doActionSetConfiguration( HttpServletRequest request, Form form, Plugin plugin,
            Plugin pluginExportdatabase )
    {
        String strTableName = request.getParameter( PARAMETER_ID_TABLE_NAME );
        String strExportAll = request.getParameter( PARAMETER_EXPORT_ALL );

        if ( ( strTableName == null ) || strTableName.equals( EMPTY_STRING ) )
        {
            return Messages.MANDATORY_FIELDS;
        }

        // Set the table name blob
        String strTableNameBlob = strTableName
                + AppPropertiesService.getProperty( PROPERTY_SUFFIX_TABLE_NAME_BLOB, DEFAULT_SUFFIX_TABLE_NAME_BLOB );

        if ( strTableNameBlob.matches( EMPTY_STRING ) )
        {
            return Messages.MANDATORY_FIELDS;
        }

        if ( !strTableName.matches( REGEX_SQL ) || !strTableNameBlob.matches( REGEX_SQL ) )
        {
            return MESSAGE_ERROR_TABLE_NAME;
        }

        FormConfiguration formConfiguration = FormConfigurationHome.findByPrimaryKey( form.getIdForm( ),
                pluginExportdatabase );

        if ( formConfiguration == null )
        {
            formConfiguration = new FormConfiguration( );
            formConfiguration.setIdForm( form.getIdForm( ) );
            formConfiguration.setTableName( strTableName );
            formConfiguration.setTableNameBlob( strTableNameBlob );
            FormConfigurationHome.insert( formConfiguration, pluginExportdatabase );
        }
        else
        {
            ExportdatabaseHome.dropTables( formConfiguration, plugin, pluginExportdatabase );
            formConfiguration.setTableName( strTableName );
            formConfiguration.setTableNameBlob( strTableNameBlob );
            FormConfigurationHome.store( formConfiguration, pluginExportdatabase );
        }

        Collection<FormConfiguration> formConfigurationList = FormConfigurationHome.findAll( pluginExportdatabase );

        for ( FormConfiguration formConfigurationTest : formConfigurationList )
        {
            if ( formConfigurationTest.getTableName( ).equalsIgnoreCase( formConfiguration.getTableName( ) )
                    && ( formConfigurationTest.getIdForm( ) != formConfiguration.getIdForm( ) ) )
            {
                return MESSAGE_TABLE_NAME_ALREADY_EXISTS;
            }
        }

        for ( IEntry entry : FormUtils.getAllQuestionList( form.getIdForm( ), plugin ) )
        {
            String strColumnName = request.getParameter( PARAMETER_ID_COLUMN_NAME_PREFIX + entry.getIdEntry( ) );

            if ( ( strColumnName == null ) || ( strColumnName.matches( EMPTY_STRING ) ) )
            {
                return Messages.MANDATORY_FIELDS;
            }

            if ( !strColumnName.matches( REGEX_SQL ) )
            {
                return MESSAGE_ERROR_COLUMN_NAME;
            }

            EntryConfiguration entryConfigurationFromEntry = EntryConfigurationHome.findByPrimaryKey(
                    form.getIdForm( ), entry.getIdEntry( ), pluginExportdatabase );

            if ( entryConfigurationFromEntry == null )
            {
                entryConfigurationFromEntry = new EntryConfiguration( );
                entryConfigurationFromEntry.setIdEntry( entry.getIdEntry( ) );
                entryConfigurationFromEntry.setIdForm( form.getIdForm( ) );
                entryConfigurationFromEntry.setColumnName( strColumnName );
                entryConfigurationFromEntry.setLongValue( isLongValue( entry ) );
                entryConfigurationFromEntry.setHasReferenceTable( hasReferenceTable( entry ) );
                EntryConfigurationHome.insert( entryConfigurationFromEntry, pluginExportdatabase );
            }
            else
            {
                entryConfigurationFromEntry.setColumnName( strColumnName );
                entryConfigurationFromEntry.setLongValue( isLongValue( entry ) );
                entryConfigurationFromEntry.setHasReferenceTable( hasReferenceTable( entry ) );
                EntryConfigurationHome.store( entryConfigurationFromEntry, pluginExportdatabase );
            }
        }

        // Create the tables
        if ( !ExportdatabaseHome.tableExists( formConfiguration.getTableName( ), pluginExportdatabase )
                && !ExportdatabaseHome.tableExists( formConfiguration.getTableNameBlob( ), pluginExportdatabase ) )
        {
            ExportdatabaseHome.createTables( formConfiguration, plugin, pluginExportdatabase );
        }

        if ( ( strExportAll != null ) && strExportAll.equals( EXPORT_ALL_YES ) )
        {
            ExportdatabaseHome.exportAllFormSubmit( form, plugin, pluginExportdatabase );
        }

        return null;
    }

    /**
     * Process deletion of export tables
     * 
     * @param request The {@link HttpServletRequest}
     * @param form The {@link Form} linked to this outputProcessor
     * @param plugin The {@link Plugin}
     * @return An error message key or null if no error
     */
    private String doActionDeleteTables( HttpServletRequest request, Form form, Plugin plugin,
            Plugin pluginExportdatabase )
    {
        FormConfiguration formConfiguration = FormConfigurationHome.findByPrimaryKey( form.getIdForm( ),
                pluginExportdatabase );

        ExportdatabaseHome.dropTables( formConfiguration, plugin, pluginExportdatabase );

        return null;
    }

    /**
     * Process deletion of configuration
     * 
     * @param request The {@link HttpServletRequest}
     * @param form The {@link Form} linked to this outputProcessor
     * @param plugin The {@link Plugin}
     * @return An error message key or null if no error
     */
    private String doActionDeleteConfiguration( HttpServletRequest request, Form form, Plugin plugin,
            Plugin pluginExportdatabase )
    {
        EntryConfigurationHome.deleteByForm( form.getIdForm( ), pluginExportdatabase );
        FormConfigurationHome.delete( form.getIdForm( ), pluginExportdatabase );

        return null;
    }

    /**
     * Process the deletion of export table + re-export all submitted forms for
     * this form
     * @param request The {@link HttpServletRequest}
     * @param form The {@link Form} linked to this outputProcessor
     * @param plugin The {@link Plugin}
     * @return An error message key or null if no error
     */
    private String doActionDeleteTablesAndExportAllFormSubmit( HttpServletRequest request, Form form, Plugin plugin,
            Plugin pluginExportdatabase )
    {
        FormConfiguration formConfiguration = FormConfigurationHome.findByPrimaryKey( form.getIdForm( ),
                pluginExportdatabase );

        ExportdatabaseHome.dropTables( formConfiguration, plugin, pluginExportdatabase );
        ExportdatabaseHome.createTables( formConfiguration, plugin, pluginExportdatabase );
        ExportdatabaseHome.exportAllFormSubmit( form, plugin, pluginExportdatabase );

        return null;
    }

    private boolean isLongValue( IEntry entry )
    {
        for ( int nEntryTypeId : arrayEntryTypeLong )
        {
            if ( entry.getEntryType( ).getIdType( ) == nEntryTypeId )
            {
                return true;
            }
        }

        return false;
    }

    private boolean hasReferenceTable( IEntry entry )
    {
        for ( int nEntryTypeId : arrayEntryTypeRef )
        {
            if ( entry.getEntryType( ).getIdType( ) == nEntryTypeId )
            {
                // Create reference table
                return true;
            }
        }

        return false;
    }
}
