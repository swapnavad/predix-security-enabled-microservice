/*
 * Copyright (c) 2017 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
 
package com.ge.predix.solsvc.boot;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ge.predix.uaa.token.lib.JsonUtils;

/**
 * 
 * @author 212421693 -
 */
public class AuthenticationFilter implements Filter
{
    private  String tokenScope= "application-read";  //$NON-NLS-1$
    
   
    /**
     * @return the tokenScope
     */
    public String getTokenScope()
    {
        return this.tokenScope;
    }

    /**
     * @param tokenscope the tokenScope to set
     */
    public void setTokenScope(String tokenScope)
    {
        this.tokenScope = tokenScope;
    }

    private static final String SECURITY_TOKEN_HEADER = "Authorization"; //$NON-NLS-1$
    private static final String authorizationSchema = "Bearer";//$NON-NLS-1$

    
    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy()
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
       String stringToken = request.getHeader(SECURITY_TOKEN_HEADER);
       
        if(StringUtils.isEmpty(stringToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new InsufficientAuthenticationException("Authorization header not found"); //$NON-NLS-1$
       }
        
        
        if (! StringUtils.startsWithIgnoreCase(stringToken, authorizationSchema)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new InsufficientAuthenticationException("Authorization schema "+authorizationSchema+" not found"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        stringToken = stringToken.trim().substring(authorizationSchema.length());
      
       Jwt accessToken = JwtHelper.decode(stringToken);
       Map<String,Object> claims= JsonUtils.readValue(accessToken.getClaims(), new TypeReference<Map<String, Object>>() {});
       Set<String> scopes = new HashSet<>();
       if (claims.containsKey("scope")) {//$NON-NLS-1$
           @SuppressWarnings("unchecked")
           Collection<String> values = (Collection<String>) claims.get("scope");//$NON-NLS-1$
           scopes.addAll(values);
       }
       // check for the right scope
       Boolean foundScope = Boolean.FALSE;
       for (String scope:scopes  ){
           if(org.apache.commons.lang.StringUtils.equalsIgnoreCase(scope,this.tokenScope)) {
               foundScope = Boolean.TRUE;
           }
           if(foundScope) break;
       }
       
       if(! foundScope) {
           
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           throw new InsufficientAuthenticationException("Authorization header does not have the required scope to access the resource"); //$NON-NLS-1$
       }
      
        chain.doFilter(req, res);
        
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig arg0)
            throws ServletException
    {
        // TODO Auto-generated method stub
        
    }

   

}
