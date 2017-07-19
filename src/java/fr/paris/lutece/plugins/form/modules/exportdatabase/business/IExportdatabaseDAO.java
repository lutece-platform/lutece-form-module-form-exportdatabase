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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;

import java.sql.Timestamp;


/**
 * Interface of export database DAO
 */
public interface IExportdatabaseDAO
{
    /**
     * Insert a new record in the table.
     * 
     * @param nIdFormSubmit the FormSubmit id
     * @param dateResponse the date of the response
     * @param strIp The Ip of the host response
     * @param strTableName The table name
     * @param listItems The list of Items
     * @param plugin The {@link Plugin}
     */
    void createRecordToTable( int nIdFormSubmit, Timestamp dateResponse, String strIp, String strTableName,
            ReferenceList listItems, Plugin plugin );

    /**
     * Store a new file in the table.
     * 
     * @param strTableName The table name
     * @param nIdFormSubmit the FormSubmit id
     * @param strEntryName The entry name
     * @param strFileName The file name
     * @param byteFileContent The file content
     * @param file the file to store
     * @param plugin The {@link Plugin}
     */
    void createFileToTable( String strTableName, int nIdFormSubmit, String strEntryName, String strFileName,
            byte[] byteFileContent, Plugin plugin );

    /**
     * Test if the table exist
     * @param strTableName The table name to test
     * @param plugin The plugin
     * @return true if the table exists or false else
     */
    boolean tableExists( String strTableName, Plugin plugin );

    /**
     * Create the tables specified by the {@link FormConfiguration} object
     * @param formConfiguration The {@link FormConfiguration} object
     * @param plugin The plugin
     */
    void createTables( FormConfiguration formConfiguration, Plugin plugin );

    /**
     * Create a reference table and insert the list of items
     * @param strExportTableName the export table name
     * @param strColumnName the column name concerned by the foreign key with
     *            the reference table
     * @param listItems the list of items to add into reference table
     * @param plugin the plugin
     */
    void createReferenceTable( String strExportTableName, String strColumnName, ReferenceList listItems, Plugin plugin );

    /**
     * Drop the export table
     * 
     * @param strTableName The name of the table to delete
     * @param plugin The {@link Plugin}
     */
    void dropTable( String strTableName, Plugin plugin );

    /**
     * Drop the reference table
     * 
     * @param strExportTableName the export table name
     * @param strColumnName the column name concerned by the foreign key with
     *            the reference table
     * @param plugin The {@link Plugin}
     */
    void dropReferenceTable( String strExportTableName, String strColumnName, Plugin plugin );

    /**
     * Get the number of submitted forms in the export table
     * 
     * @param formConfiguration The {@link FormConfiguration}
     * @param plugin The {@link Plugin}
     * @return the number of submitted forms
     */
    int countFormSubmit( FormConfiguration formConfiguration, Plugin plugin );
}
