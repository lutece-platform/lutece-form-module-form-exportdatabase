/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class represent the {@link EntryConfiguration} DAO
 *
 */
public class EntryConfigurationDAO implements IEntryConfigurationDAO
{
    private static final String SQL_QUERY_NEW_POSITION = "SELECT MAX(id_position) " +
        "FROM form_exportdatabase_entryconfiguration WHERE id_form = ? ";
    private static final String SQL_QUERY_SELECT_BY_PRIMARY_KEY = " SELECT column_name, is_long_value, has_ref_table " +
        "FROM form_exportdatabase_entryconfiguration WHERE id_form = ? AND id_entry = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO form_exportdatabase_entryconfiguration " +
        "( id_form, id_entry, column_name, is_long_value, has_ref_table, id_position ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_UPDATE = " UPDATE form_exportdatabase_entryconfiguration " +
        "SET column_name = ?, is_long_value = ?, has_ref_table = ? WHERE id_form = ? AND id_entry = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM form_exportdatabase_entryconfiguration " +
        "WHERE id_form = ? AND id_entry = ? ";
    private static final String SQL_QUERY_DELETE_BY_FORM = " DELETE FROM form_exportdatabase_entryconfiguration " +
        "WHERE id_form = ? ";
    private static final String SQL_QUERY_SELECT_ENTRY_CONFIGURATION_BY_ID_FORM = " SELECT id_entry, column_name, is_long_value, has_ref_table " +
        "FROM form_exportdatabase_entryconfiguration WHERE id_form = ? ORDER BY id_position ";

    /**
     * Generates a new {@link EntryConfiguration} position
     *
     * @param nIdForm The Form identifier
     * @param plugin the plugin
     * @return the new {@link EntryConfiguration} position
     */
    private int newPosition( int nIdForm, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_POSITION, plugin );
        daoUtil.setInt( 1, nIdForm );
        daoUtil.executeQuery(  );

        int nPos;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nPos = 1;
        }

        nPos = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nPos;
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.IEntryConfigurationDAO#findEntryConfigurationListByIdForm(int, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public Collection<EntryConfiguration> findEntryConfigurationListByIdForm( int nIdForm, Plugin plugin )
    {
        Collection<EntryConfiguration> entryConfigurationList = new ArrayList<EntryConfiguration>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ENTRY_CONFIGURATION_BY_ID_FORM, plugin );
        daoUtil.setInt( 1, nIdForm );

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            EntryConfiguration entryConfiguration = new EntryConfiguration(  );

            entryConfiguration.setIdEntry( daoUtil.getInt( 1 ) );
            entryConfiguration.setColumnName( daoUtil.getString( 2 ) );
            entryConfiguration.setLongValue( daoUtil.getBoolean( 3 ) );
            entryConfiguration.setHasReferenceTable( daoUtil.getBoolean( 4 ) );
            entryConfigurationList.add( entryConfiguration );
        }

        daoUtil.free(  );

        return entryConfigurationList;
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.IEntryConfiguration#findByPrimaryKey(int, int, fr.paris.lutece.portal.service.plugin.Plugin)
         */
    public EntryConfiguration findByPrimaryKey( int nIdForm, int nIdEntry, Plugin plugin )
    {
        EntryConfiguration entryConfiguration = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nIdForm );
        daoUtil.setInt( 2, nIdEntry );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            entryConfiguration = new EntryConfiguration(  );
            entryConfiguration.setIdForm( nIdForm );
            entryConfiguration.setIdEntry( nIdEntry );
            entryConfiguration.setColumnName( daoUtil.getString( 1 ) );
            entryConfiguration.setLongValue( daoUtil.getBoolean( 2 ) );
            entryConfiguration.setHasReferenceTable( daoUtil.getBoolean( 3 ) );
        }

        daoUtil.free(  );

        return entryConfiguration;
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.IEntryConfiguration#delete(int, int, fr.paris.lutece.portal.service.plugin.Plugin)
         */
    public void delete( int nIdForm, int nIdEntry, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdForm );
        daoUtil.setInt( 2, nIdEntry );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.IEntryConfiguration#deleteByForm(int, fr.paris.lutece.portal.service.plugin.Plugin)
         */
    public void deleteByForm( int nIdForm, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_FORM, plugin );
        daoUtil.setInt( 1, nIdForm );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.IEntryConfiguration#insert(fr.paris.lutece.plugins.form.modules.exportdatabase.business.EntryConfiguration, fr.paris.lutece.portal.service.plugin.Plugin)
         */
    public void insert( EntryConfiguration entryConfiguration, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        daoUtil.setInt( 1, entryConfiguration.getIdForm(  ) );
        daoUtil.setInt( 2, entryConfiguration.getIdEntry(  ) );
        daoUtil.setString( 3, entryConfiguration.getColumnName(  ) );
        daoUtil.setBoolean( 4, entryConfiguration.isLongValue(  ) );
        daoUtil.setBoolean( 5, entryConfiguration.hasReferenceTable(  ) );
        daoUtil.setInt( 6, newPosition( entryConfiguration.getIdForm(  ), plugin ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.IEntryConfiguration#store(fr.paris.lutece.plugins.form.modules.exportdatabase.business.EntryConfiguration, fr.paris.lutece.portal.service.plugin.Plugin)
         */
    public void store( EntryConfiguration entryConfiguration, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setString( 1, entryConfiguration.getColumnName(  ) );
        daoUtil.setBoolean( 2, entryConfiguration.isLongValue(  ) );
        daoUtil.setBoolean( 3, entryConfiguration.hasReferenceTable(  ) );
        daoUtil.setInt( 4, entryConfiguration.getIdForm(  ) );
        daoUtil.setInt( 5, entryConfiguration.getIdEntry(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
