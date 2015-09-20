/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.loway.oss.ari4java.codegen2.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lenz
 * @version $Id$
 */
public class Api {

    public String path = "";
    public String description = "";
    public List<ApiOperation> operations = new ArrayList<ApiOperation>();
}

// $Log$
//
