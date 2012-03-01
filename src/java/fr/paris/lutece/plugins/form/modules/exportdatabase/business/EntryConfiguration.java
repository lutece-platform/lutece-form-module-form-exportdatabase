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

import fr.paris.lutece.plugins.form.business.EntryHome;
import fr.paris.lutece.plugins.form.business.IEntry;
import fr.paris.lutece.plugins.form.service.EntryRemovalListenerService;
import fr.paris.lutece.plugins.form.service.FormPlugin;
import fr.paris.lutece.portal.service.plugin.PluginService;


/**
 *
 * @author ELY
 *
 */
public class EntryConfiguration
{
    private static EntryConfigurationEntryRemovalListener _listenerForm;
    private int _nIdForm;
    private int _nIdEntry;
    private String _strColumnName;
    private boolean _bLongValue;
    private boolean _bHasReferenceTable;

    /**
         * @return the _bHasReferenceTable
         */
    public boolean hasReferenceTable(  )
    {
        return _bHasReferenceTable;
    }

    /**
     * @param bHasReferenceTable the _bHasReferenceTable to set
     */
    public void setHasReferenceTable( boolean bHasReferenceTable )
    {
        this._bHasReferenceTable = bHasReferenceTable;
    }

    /**
    * Initialize the EntryConfiguration
    */
    public static void init(  )
    {
        // Create removal listeners and register them
        if ( _listenerForm == null )
        {
            _listenerForm = new EntryConfigurationEntryRemovalListener(  );
            EntryRemovalListenerService.getService(  ).registerListener( _listenerForm );
        }
    }

    /**
     * @return the idForm
     */
    public int getIdForm(  )
    {
        return _nIdForm;
    }

    /**
     * @param nIdForm the idForm to set
     */
    public void setIdForm( int nIdForm )
    {
        this._nIdForm = nIdForm;
    }

    /**
     * @return the idEntry
     */
    public int getIdEntry(  )
    {
        return _nIdEntry;
    }

    /**
     * @param idEntry the idEntry to set
     */
    public void setIdEntry( int idEntry )
    {
        this._nIdEntry = idEntry;
    }

    /**
     * @return the column name
     */
    public String getColumnName(  )
    {
        return _strColumnName;
    }

    /**
     * @param strColumnName the column name to set
     */
    public void setColumnName( String strColumnName )
    {
        this._strColumnName = strColumnName;
    }

    /**
     * Get the Entry title
     * @return The entry title
     */
    public String getEntryTitle(  )
    {
        IEntry entry = EntryHome.findByPrimaryKey( getIdEntry(  ), PluginService.getPlugin( FormPlugin.PLUGIN_NAME ) );

        if ( entry != null )
        {
            return entry.getTitle(  );
        }
        else
        {
            return null;
        }
    }

    /**
     * @return the bLongValue
     */
    public boolean isLongValue(  )
    {
        return _bLongValue;
    }

    /**
     * @param bLongValue the bLongValue to set
     */
    public void setLongValue( boolean bLongValue )
    {
        this._bLongValue = bLongValue;
    }
}
