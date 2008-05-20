//
// This file is part of InfoGrid(tm). You may not use this file except in
// compliance with the InfoGrid license. The InfoGrid license and important
// disclaimers are contained in the file LICENSE.InfoGrid.txt that you should
// have received with InfoGrid. If you have not received LICENSE.InfoGrid.txt
// or you do not consent to all aspects of the license and the disclaimers,
// no license is granted; do not use this file.
// 
// For more information about InfoGrid go to http://infogrid.org/
//
// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh.set.active.m;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSetFactory;

/**
 * A degenerated ActiveMeshObjectSet which has constant content. While this may
 * sound a bit crazy, it does happen in practice that a constant set is created and
 * used where an ActiveMeshObjectSet is expected. This implementation fills this need.
 */
public class ConstantActiveMMeshObjectSet
        extends
            AbstractActiveMMeshObjectSet
{
    /**
     * Private constructor, use factory methods.
     *
     * @param factory the MeshObjectSetFactory that created this MeshObjectSet
     * @param content the content of this set
     */
    protected ConstantActiveMMeshObjectSet(
            MeshObjectSetFactory factory,
            MeshObject []        content )
    {
        super( factory );
        
        setInitialContent( content );
    }
}
