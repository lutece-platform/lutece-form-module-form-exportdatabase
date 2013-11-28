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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * 
 * @author ELY
 * 
 */
public class FormConfigurationDAO implements IFormConfigurationDAO
{
    private static final String SQL_QUERY_SELECT_ALL = " SELECT id_form, table_name, table_name_blob FROM form_exportdatabase_formconfiguration ";
    private static final String SQL_QUERY_SELECT_BY_PRIMARY_KEY = " SELECT table_name, table_name_blob FROM form_exportdatabase_formconfiguration WHERE id_form = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO form_exportdatabase_formconfiguration ( id_form, table_name, table_name_blob ) VALUES ( ?, ?, ? )";
    private static final String SQL_QUERY_UPDATE = " UPDATE form_exportdatabase_formconfiguration SET table_name = ?, table_name_blob = ? WHERE id_form = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM form_exportdatabase_formconfiguration WHERE id_form = ?";

    /**
     * Find all {@link FormConfiguration}
     * 
     * @param plugin The {@link Plugin}
     * @return The {@link FormConfiguration} or null if not exists
     */
    public Collection<FormConfiguration> findAll( Plugin plugin )
    {
        Collection<FormConfiguration> formConfigurationList = new ArrayList<FormConfiguration>( );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL, plugin );

        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            FormConfiguration formConfiguration = new FormConfiguration( );
            formConfiguration.setIdForm( daoUtil.getInt( 1 ) );
            formConfiguration.setTableName( daoUtil.getString( 2 ) );
            formConfiguration.setTableNameBlob( daoUtil.getString( 3 ) );
            formConfigurationList.add( formConfiguration );
        }

        daoUtil.free( );

        return formConfigurationList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.
     * IFormConfigurationDAO#findByPrimaryKey(int,
     * fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public FormConfiguration findByPrimaryKey( int nIdForm, Plugin plugin )
    {
        FormConfiguration formConfiguration = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nIdForm );

        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            formConfiguration = new FormConfiguration( );
            formConfiguration.setIdForm( nIdForm );
            formConfiguration.setTableName( daoUtil.getString( 1 ) );
            formConfiguration.setTableNameBlob( daoUtil.getString( 2 ) );
        }

        daoUtil.free( );

        return formConfiguration;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.
     * IFormConfigurationDAO#delete(int,
     * fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void delete( int nIdForm, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdForm );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.
     * IFormConfigurationDAO
     * #insert(fr.paris.lutece.plugins.form.modules.exportdatabase
     * .business.FormConfiguration,
     * fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void insert( FormConfiguration formConfiguration, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        daoUtil.setInt( 1, formConfiguration.getIdForm( ) );
        daoUtil.setString( 2, formConfiguration.getTableName( ) );
        daoUtil.setString( 3, formConfiguration.getTableNameBlob( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.
     * IFormConfigurationDAO
     * #store(fr.paris.lutece.plugins.form.modules.exportdatabase
     * .business.FormConfiguration,
     * fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void store( FormConfiguration formConfiguration, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setString( 1, formConfiguration.getTableName( ) );
        daoUtil.setString( 2, formConfiguration.getTableNameBlob( ) );
        daoUtil.setInt( 3, formConfiguration.getIdForm( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }
}
