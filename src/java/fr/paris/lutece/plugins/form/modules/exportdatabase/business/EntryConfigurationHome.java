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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;


/**
 * 
 * @author ELY
 * 
 */
public class EntryConfigurationHome
{
    // Static variable pointed at the DAO instance
    private static IEntryConfigurationDAO _dao = SpringContextService
            .getBean( "form-exportdatabase.entryConfigurationDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private EntryConfigurationHome( )
    {
    }

    /**
     * Find the {@link EntryConfiguration} by ids
     * 
     * @param nIdForm The form id
     * @param nIdEntry The entry id
     * @param plugin The {@link Plugin}
     * @return The {@link EntryConfiguration} or null if not exists
     */
    public static EntryConfiguration findByPrimaryKey( int nIdForm, int nIdEntry, Plugin plugin )
    {
        return _dao.findByPrimaryKey( nIdForm, nIdEntry, plugin );
    }

    /**
     * Find the {@link EntryConfiguration} list from the form identifier
     * 
     * @param nIdForm The form identifier
     * @param plugin The {@link Plugin}
     * @return The {@link Collection} of {@link EntryConfiguration}
     */
    public static Collection<EntryConfiguration> findEntryConfigurationListByIdForm( int nIdForm, Plugin plugin )
    {
        return _dao.findEntryConfigurationListByIdForm( nIdForm, plugin );
    }

    /**
     * Delete an Entry configuration
     * 
     * @param nIdForm The form identifier
     * @param nIdEntry The entry identifier
     * @param plugin The plugin
     */
    public static void delete( int nIdForm, int nIdEntry, Plugin plugin )
    {
        _dao.delete( nIdForm, nIdEntry, plugin );
    }

    /**
     * Delete all Entry configuration by Form
     * 
     * @param nIdForm The form identifier
     * @param plugin The plugin
     */
    public static void deleteByForm( int nIdForm, Plugin plugin )
    {
        _dao.deleteByForm( nIdForm, plugin );
    }

    /**
     * Insert a new Entry configuration into database
     * 
     * @param entryConfiguration The new {@link EntryConfiguration}
     * @param plugin The plugin
     */
    public static void insert( EntryConfiguration entryConfiguration, Plugin plugin )
    {
        _dao.insert( entryConfiguration, plugin );
    }

    /**
     * Update an existing {@link EntryConfiguration}
     * 
     * @param entryConfiguration The {@link EntryConfiguration} to update
     * @param plugin The plugin
     */
    public static void store( EntryConfiguration entryConfiguration, Plugin plugin )
    {
        _dao.store( entryConfiguration, plugin );
    }
}
