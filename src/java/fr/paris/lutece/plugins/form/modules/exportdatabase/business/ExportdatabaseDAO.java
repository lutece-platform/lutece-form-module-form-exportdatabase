/*
 * Copyright (c) 2002-2011, Mairie de Paris
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
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;

import java.util.Collection;


/**
 * This class represent the {@link ExportdatabaseDAO} DAO
 *
 */
public class ExportdatabaseDAO implements IExportdatabaseDAO
{
    private static final String SQL_QUERY_INSERT_PREFIX = " INSERT INTO ";
    private static final String SQL_QUERY_OPEN_BRACKET = " ( ";
    private static final String SQL_QUERY_ID_FORMSUBMIT = " id_formsubmit ";
    private static final String SQL_QUERY_ID_DATE_RESPONSE = " date_response ";
    private static final String SQL_QUERY_IP = " ip ";
    private static final String SQL_QUERY_COMMA = ", ";
    private static final String SQL_QUERY_CLOSE_BRACKET = " ) ";
    private static final String SQL_QUERY_INSERT_VALUES = " VALUES ( ";
    private static final String SQL_QUERY_QUESTION = " ? ";
    private static final String EMPTY_STRING = "";
    private static final String SQL_QUERY_END = "; ";
    private static final String SQL_QUERY_NOT_NULL = " NOT NULL ";
    private static final String SQL_QUERY_INSERT_FILE_SUFFIX = " (id_formsubmit, entry_name, file_name, file_content) VALUES (?, ?, ?, ?) ";
    private static final String SQL_SHOW_TABLE = " SHOW TABLES LIKE ? ";
    private static final String SQL_QUERY_CREATE_TABLE = " CREATE TABLE ";
    private static final String SQL_QUERY_CREATE_TABLE_BEGIN = " ( id_formsubmit INT NOT NULL," +
        "date_response TIMESTAMP, ip VARCHAR(100), ";
    private static final String SQL_QUERY_CREATE_TABLE_TYPE_SHORT = " VARCHAR(255) ";
    private static final String SQL_QUERY_CREATE_TABLE_TYPE_LONG = " LONG VARCHAR ";
    private static final String SQL_QUERY_CREATE_TABLE_PK = " PRIMARY KEY(id_formsubmit) ";
    private static final String SQL_QUERY_CREATE_TABLE_CONSTRAINT = " CONSTRAINT ";
    private static final String SQL_QUERY_CREATE_TABLE_FK_1 = " FOREIGN KEY (";
    private static final String SQL_QUERY_CREATE_TABLE_FK_2 = ")  REFERENCES ";
    private static final String SQL_QUERY_CREATE_TABLE_FK_3 = "(ref_key) ";
    private static final String SQL_QUERY_CREATE_TABLE_END_CREATE = " )";
    private static final String SQL_QUERY_CREATE_TABLE_BLOB_END = " ( id_formsubmit INT NOT NULL, " +
        "entry_name VARCHAR(255) NOT NULL, file_name VARCHAR(100) NOT NULL, " +
        "file_content LONG VARBINARY NOT NULL, PRIMARY KEY(id_formsubmit, entry_name)); ";
    private static final String SQL_QUERY_DROP_TABLE = " DROP TABLE IF EXISTS ";
    private static final String SQL_QUERY_COUNT_FORM_SUBMIT = " SELECT COUNT(id_formsubmit) FROM ";
    private static final String SQL_QUERY_CREATE_REFERENCE_TABLE = "( ref_key VARCHAR(255), ref_value VARCHAR(255), PRIMARY KEY(ref_key));";
    private static final String SQL_QUERY_INSERT_REFERENCE_TABLE = " ( ref_key, ref_value ) VALUE ( ?, ? );";
    private static final String FOREIGN_KEY_PREFIX = "fk_";
    private static final String TAG_REF_TABLE = "_ref_";

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.IExportdatabaseDAO#createRecordToTable(int, java.lang.String, fr.paris.lutece.util.ReferenceList, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void createRecordToTable( int nIdFormSubmit, Timestamp dateResponse, String strIp, String strTableName,
        ReferenceList listItems, Plugin plugin )
    {
        DAOUtil daoUtil = getDaoFromReferenceList( nIdFormSubmit, dateResponse, strIp, strTableName, listItems, plugin );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.IExportdatabaseDAO#createFileToTable(java.lang.String, int, java.lang.String, java.lang.String, byte[], fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void createFileToTable( String strTableName, int nIdFormSubmit, String strEntryName, String strFileName,
        byte[] byteFileContent, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_PREFIX + strTableName + SQL_QUERY_INSERT_FILE_SUFFIX, plugin );
        int nIndex = 1;
        daoUtil.setInt( nIndex++, nIdFormSubmit );
        daoUtil.setString( nIndex++, strEntryName );
        daoUtil.setString( nIndex++, strFileName );
        daoUtil.setBytes( nIndex++, byteFileContent );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.IExportdatabaseDAO#tableExists(java.lang.String, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public boolean tableExists( String strTableName, Plugin plugin )
    {
        if ( strTableName == null )
        {
            return false;
        }

        boolean bReturn = false;
        DAOUtil daoUtil = new DAOUtil( SQL_SHOW_TABLE, plugin );
        daoUtil.setString( 1, strTableName );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            bReturn = true;
        }

        daoUtil.free(  );

        return bReturn;
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.IExportdatabaseDAO#createTables(fr.paris.lutece.plugins.form.modules.exportdatabase.business.FormConfiguration, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void createTables( FormConfiguration formConfiguration, Plugin plugin )
    {
        DAOUtil daoUtilTable = new DAOUtil( getSqlCreateTable( formConfiguration, plugin ), plugin );
        daoUtilTable.executeUpdate(  );
        daoUtilTable.free(  );

        DAOUtil daoUtilTableBlob = new DAOUtil( getSqlCreateTableBlob( formConfiguration, plugin ), plugin );
        daoUtilTableBlob.executeUpdate(  );
        daoUtilTableBlob.free(  );
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.IExportdatabaseDAO#createReferenceTable(java.lang.String, fr.paris.lutece.util.ReferenceList, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void createReferenceTable( String strExportTableName, String strColumnName, ReferenceList listItems,
        Plugin plugin )
    {
        String strReferenceTableName = getReferenceTableName( strExportTableName, strColumnName );
        DAOUtil daoUtilTable = new DAOUtil( getSqlCreateReferenceTable( strReferenceTableName, listItems, plugin ),
                plugin );
        daoUtilTable.executeUpdate(  );
        daoUtilTable.free(  );

        for ( ReferenceItem item : listItems )
        {
            DAOUtil daoUtilInsert = new DAOUtil( SQL_QUERY_INSERT_PREFIX + strReferenceTableName +
                    SQL_QUERY_INSERT_REFERENCE_TABLE, plugin );
            daoUtilInsert.setString( 1, item.getCode(  ) );
            daoUtilInsert.setString( 2, item.getName(  ) );
            daoUtilInsert.executeUpdate(  );
            daoUtilInsert.free(  );
        }
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.IExportdatabaseDAO#dropTable(java.lang.String, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void dropTable( String strTableName, Plugin plugin )
    {
        DAOUtil daoUtilTable = new DAOUtil( SQL_QUERY_DROP_TABLE + strTableName + SQL_QUERY_END, plugin );
        daoUtilTable.executeUpdate(  );
        daoUtilTable.free(  );
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.IExportdatabaseDAO#dropReferenceTable(java.lang.String, java.lang.String, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void dropReferenceTable( String strExportTableName, String strColumnName, Plugin plugin )
    {
        DAOUtil daoUtilTable = new DAOUtil( SQL_QUERY_DROP_TABLE +
                getReferenceTableName( strExportTableName, strColumnName ) + SQL_QUERY_END, plugin );
        daoUtilTable.executeUpdate(  );
        daoUtilTable.free(  );
    }

    /*
     * (non-Javadoc)
     * @see fr.paris.lutece.plugins.form.modules.exportdatabase.business.IExportdatabaseDAO#countFormSubmit(fr.paris.lutece.plugins.form.modules.exportdatabase.business.FormConfiguration, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public int countFormSubmit( FormConfiguration formConfiguration, Plugin plugin )
    {
        int nCount = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_FORM_SUBMIT + formConfiguration.getTableName(  ), plugin );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nCount = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nCount;
    }

    /**
     * Create the create table sql query
     * @param formConfiguration The {@link FormConfiguration}
     * @param plugin The {@link Plugin}
     * @return The Sql query
     */
    private String getSqlCreateTable( FormConfiguration formConfiguration, Plugin plugin )
    {
        String strSQL = SQL_QUERY_CREATE_TABLE + formConfiguration.getTableName(  ) + SQL_QUERY_CREATE_TABLE_BEGIN;
        Collection<EntryConfiguration> entryList = formConfiguration.getEntryTextConfigurationList( plugin );

        for ( EntryConfiguration entryConfiguration : entryList )
        {
            strSQL += entryConfiguration.getColumnName(  );

            if ( entryConfiguration.isLongValue(  ) )
            {
                strSQL += SQL_QUERY_CREATE_TABLE_TYPE_LONG;
            }
            else
            {
                strSQL += SQL_QUERY_CREATE_TABLE_TYPE_SHORT;
            }

            strSQL += ( SQL_QUERY_NOT_NULL + SQL_QUERY_COMMA );
        }

        strSQL += SQL_QUERY_CREATE_TABLE_PK;
    	/**
        for ( EntryConfiguration entryConfiguration : entryList )
        {
           
        
        	if ( entryConfiguration.hasReferenceTable(  ) )
            {
                if ( !strSQL.equals( EMPTY_STRING ) )
                {
                    strSQL += SQL_QUERY_COMMA;
                }

                String strReferenceTableName = getReferenceTableName( formConfiguration.getTableName(  ),
                        entryConfiguration.getColumnName(  ) );
                String strConstraintName = FOREIGN_KEY_PREFIX + strReferenceTableName;
                strSQL += ( SQL_QUERY_CREATE_TABLE_CONSTRAINT + strConstraintName + SQL_QUERY_CREATE_TABLE_FK_1 +
                entryConfiguration.getColumnName(  ) + SQL_QUERY_CREATE_TABLE_FK_2 + strReferenceTableName +
                SQL_QUERY_CREATE_TABLE_FK_3 );
            }
           
        }
    	 	**/
        strSQL += ( SQL_QUERY_CREATE_TABLE_END_CREATE + SQL_QUERY_END );

        return strSQL;
    }

    /**
     * Create the create reference table sql query
     * @param strTableName The table name
     * @param listItems The list of items
     * @param plugin The {@link Plugin}
     * @return The Sql query
     */
    private String getSqlCreateReferenceTable( String strTableName, ReferenceList listItems, Plugin plugin )
    {
        String strSQL = SQL_QUERY_CREATE_TABLE + strTableName + SQL_QUERY_CREATE_REFERENCE_TABLE;

        return strSQL;
    }

    /**
     * Create the blob table sql query
     *
     * @param formConfiguration The {@link FormConfiguration}
     * @param plugin The {@link Plugin}
     * @return The Sql query
     */
    private String getSqlCreateTableBlob( FormConfiguration formConfiguration, Plugin plugin )
    {
        return SQL_QUERY_CREATE_TABLE + formConfiguration.getTableNameBlob(  ) + SQL_QUERY_CREATE_TABLE_BLOB_END;
    }

    /**
     * Return a DAO initialized with the specified filter
     * @param nIdFormSubmit The formSubmit id
     * @param dateResponse the date response
     * @param strIp the response IP
     * @param strQuerySelect the query
     * @param filter the {@link QuicklinksFilter} object
     * @return the DaoUtil
     */
    private DAOUtil getDaoFromReferenceList( int nIdFormSubmit, Timestamp dateResponse, String strIp,
        String strTableName, ReferenceList listItems, Plugin plugin )
    {
        String strSQL = SQL_QUERY_INSERT_PREFIX + strTableName;
        String strColumnsList = EMPTY_STRING;
        String strValues = EMPTY_STRING;

        // id_formsubmit
        strColumnsList += ( ( ( !strColumnsList.equals( EMPTY_STRING ) ) ? SQL_QUERY_COMMA : EMPTY_STRING ) +
        ( SQL_QUERY_ID_FORMSUBMIT ) );
        strValues += ( ( ( !strValues.equals( EMPTY_STRING ) ) ? SQL_QUERY_COMMA : EMPTY_STRING ) +
        ( SQL_QUERY_QUESTION ) );
        // date_response
        strColumnsList += ( ( ( !strColumnsList.equals( EMPTY_STRING ) ) ? SQL_QUERY_COMMA : EMPTY_STRING ) +
        ( SQL_QUERY_ID_DATE_RESPONSE ) );
        strValues += ( ( ( !strValues.equals( EMPTY_STRING ) ) ? SQL_QUERY_COMMA : EMPTY_STRING ) +
        ( SQL_QUERY_QUESTION ) );
        // ip
        strColumnsList += ( ( ( !strColumnsList.equals( EMPTY_STRING ) ) ? SQL_QUERY_COMMA : EMPTY_STRING ) +
        ( SQL_QUERY_IP ) );
        strValues += ( ( ( !strValues.equals( EMPTY_STRING ) ) ? SQL_QUERY_COMMA : EMPTY_STRING ) +
        ( SQL_QUERY_QUESTION ) );

        for ( ReferenceItem item : listItems )
        {
            strColumnsList += ( ( ( !strColumnsList.equals( EMPTY_STRING ) ) ? SQL_QUERY_COMMA : EMPTY_STRING ) +
            ( item.getCode(  ) ) );
            strValues += ( ( ( !strValues.equals( EMPTY_STRING ) ) ? SQL_QUERY_COMMA : EMPTY_STRING ) +
            ( SQL_QUERY_QUESTION ) );
        }

        strColumnsList = SQL_QUERY_OPEN_BRACKET + strColumnsList + SQL_QUERY_CLOSE_BRACKET;
        strValues = SQL_QUERY_INSERT_VALUES + strValues + SQL_QUERY_CLOSE_BRACKET;
        strSQL += ( strColumnsList + strValues );
        AppLogService.debug( "Sql query form exportdatabase : " + strSQL );

        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );
        int nIndex = 1;

        daoUtil.setInt( nIndex++, nIdFormSubmit );
        daoUtil.setTimestamp( nIndex++, dateResponse );
        daoUtil.setString( nIndex++, strIp );

        for ( ReferenceItem item : listItems )
        {
            daoUtil.setString( nIndex, item.getName(  ) );
            AppLogService.debug( "Param" + nIndex + " (" + item.getCode(  ) + ") = " + item.getName(  ) );
            nIndex++;
        }

        return daoUtil;
    }

    /**
     * Set the reference table name
     * @param strExportTableName The name of the export table
     * @param strColumnName The name of the column concerned by the foreign key with the reference table
     * @return The reference table name
     */
    private String getReferenceTableName( String strExportTableName, String strColumnName )
    {
        return strExportTableName + TAG_REF_TABLE + strColumnName;
    }
}
