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
package fr.paris.lutece.plugins.form.modules.exportdatabase.web;

import fr.paris.lutece.plugins.form.business.FormSubmit;
import fr.paris.lutece.plugins.form.business.FormSubmitHome;
import fr.paris.lutece.plugins.form.modules.exportdatabase.business.ExportdatabaseHome;
import fr.paris.lutece.plugins.form.modules.exportdatabase.service.ExportdatabasePlugin;
import fr.paris.lutece.plugins.form.modules.exportdatabase.util.StringUtil;
import fr.paris.lutece.plugins.form.service.FormPlugin;
import fr.paris.lutece.plugins.genericattributes.business.Response;
import fr.paris.lutece.plugins.genericattributes.business.ResponseFilter;
import fr.paris.lutece.plugins.genericattributes.business.ResponseHome;
import fr.paris.lutece.plugins.genericattributes.service.entrytype.EntryTypeServiceManager;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.PluginService;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


/**
 * 
 * @author ELY
 * 
 */
public class ExportdatabaseJspBean
{
    /**
     * Test the table insertion
     * 
     * @param request The {@link HttpServletRequest}
     */
    public void testCreation( HttpServletRequest request )
    {
        FormSubmit formSubmit = FormSubmitHome.findByPrimaryKey( 1, PluginService.getPlugin( FormPlugin.PLUGIN_NAME ) );
        ResponseFilter filter = new ResponseFilter( );
        filter.setIdResource( 1 );
        formSubmit.setListResponse( ResponseHome.getResponseList( filter ) );

        for ( Response response : formSubmit.getListResponse( ) )
        {
            byte[] byResponseValue = StringUtil.convertToByte( response.getResponseValue( ) );

            if ( byResponseValue != null )
            {
                response.setToStringValueResponse( EntryTypeServiceManager.getEntryTypeService( response.getEntry( ) )
                        .getResponseValueForRecap( response.getEntry( ), request, response,
                                I18nService.getDefaultLocale( ) ) );
            }
            else
            {
                response.setToStringValueResponse( StringUtils.EMPTY );
            }
        }

        ExportdatabaseHome.addFormSubmit( formSubmit, PluginService.getPlugin( FormPlugin.PLUGIN_NAME ),
                PluginService.getPlugin( ExportdatabasePlugin.PLUGIN_NAME ) );
    }
}
