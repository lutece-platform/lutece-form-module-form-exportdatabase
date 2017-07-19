/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import fr.paris.lutece.plugins.form.service.FormRemovalListenerService;
import fr.paris.lutece.plugins.genericattributes.business.EntryHome;
import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;


/**
 * 
 * @author ELY
 * 
 */
public class FormConfiguration
{
    private static FormConfigurationFormRemovalListener _listenerForm;
    private int _nIdForm;
    private String _strTableName;
    private String _strTableNameBlob;

    /**
     * Initialize the FormConfiguration
     */
    public static void init( )
    {
        // Create removal listeners and register them
        if ( _listenerForm == null )
        {
            _listenerForm = new FormConfigurationFormRemovalListener( );
            FormRemovalListenerService.getService( ).registerListener( _listenerForm );
        }
    }

    /**
     * @return the idForm
     */
    public int getIdForm( )
    {
        return _nIdForm;
    }

    /**
     * @param idForm the idForm to set
     */
    public void setIdForm( int idForm )
    {
        this._nIdForm = idForm;
    }

    /**
     * @return the tableName
     */
    public String getTableName( )
    {
        return _strTableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName( String tableName )
    {
        this._strTableName = tableName;
    }

    /**
     * @return the tableNameBlob
     */
    public String getTableNameBlob( )
    {
        return _strTableNameBlob;
    }

    /**
     * @param strTableNameBlob the tableName to set
     */
    public void setTableNameBlob( String strTableNameBlob )
    {
        this._strTableNameBlob = strTableNameBlob;
    }

    /**
     * Get the list of {@link EntryConfiguration} objects
     * 
     * @param plugin The {@link Plugin}
     * @return The {@link Collection} of {@link EntryConfiguration}
     */
    public Collection<EntryConfiguration> getEntryConfigurationList( Plugin plugin )
    {
        return EntryConfigurationHome.findEntryConfigurationListByIdForm( getIdForm( ), plugin );
    }

    /**
     * Get the list of {@link EntryConfiguration} objects without entry type
     * blob
     * 
     * @param plugin The {@link Plugin}
     * @return The {@link Collection} of {@link EntryConfiguration}
     */
    public Collection<EntryConfiguration> getEntryTextConfigurationList( Plugin plugin )
    {
        Collection<EntryConfiguration> entryConfigurationList = EntryConfigurationHome
                .findEntryConfigurationListByIdForm( getIdForm( ), plugin );
        Collection<EntryConfiguration> entryTextConfigurationList = new ArrayList<EntryConfiguration>( );

        for ( EntryConfiguration entryConfiguration : entryConfigurationList )
        {
            Entry entry = EntryHome.findByPrimaryKey( entryConfiguration.getIdEntry( ) );

            if ( entry.getEntryType( ).getIdType( ) != ExportdatabaseHome.ENTRY_TYPE_FILE_IDENTIFIER )
            {
                entryTextConfigurationList.add( entryConfiguration );
            }
        }

        return entryTextConfigurationList;
    }
}
