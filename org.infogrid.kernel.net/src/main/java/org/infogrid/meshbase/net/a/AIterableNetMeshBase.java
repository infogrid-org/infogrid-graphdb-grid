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
// Copyright 1998-2015 by Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.net.a;

import java.util.HashMap;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.meshbase.net.IterableNetMeshBase;
import org.infogrid.meshbase.net.IterableNetMeshBaseDifferencer;
import org.infogrid.meshbase.net.NetMeshBaseIdentifier;
import org.infogrid.meshbase.net.NetMeshBaseIdentifierFactory;
import org.infogrid.meshbase.net.NetMeshObjectAccessSpecificationFactory;
import org.infogrid.meshbase.net.NetMeshObjectIdentifierFactory;
import org.infogrid.meshbase.net.proxy.ProxyManager;
import org.infogrid.meshbase.net.security.NetAccessManager;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.util.CachingMap;
import org.infogrid.util.CursorIterator;
import org.infogrid.util.MapCursorIterator;
import org.infogrid.util.context.Context;

/**
 * The subtype of IterableMeshBase suitable for the AnetMeshObject implementation.
 */
public abstract class AIterableNetMeshBase
        extends
            AnetMeshBase
        implements
            IterableNetMeshBase
{
    /**
     * Constructor for subclasses only. This does not initialize content.
     *
     * @param identifier the NetMeshBaseIdentifier of this NetMeshBase
     * @param identifierFactory the factory for NetMeshObjectIdentifiers appropriate for this NetMeshBase
     * @param meshBaseIdentifierFactory the factory for NetMeshBaseIdentifiers
     * @param netMeshObjectAccessSpecificationFactory the factory for NetMeshObjectAccessSpecifications
     * @param setFactory the factory for MeshObjectSets appropriate for this NetMeshBase
     * @param modelBase the ModelBase containing type information
     * @param life the MeshBaseLifecycleManager to use
     * @param accessMgr the AccessManager that controls access to this NetMeshBase
     * @param cache the CachingMap that holds the NetMeshObjects in this NetMeshBase
     * @param proxyManager the ProxyManager used by this NetMeshBase
     * @param context the Context in which this NetMeshBase runs.
     */
    protected AIterableNetMeshBase(
            NetMeshBaseIdentifier                       identifier,
            NetMeshObjectIdentifierFactory              identifierFactory,
            NetMeshBaseIdentifierFactory                meshBaseIdentifierFactory,
            NetMeshObjectAccessSpecificationFactory     netMeshObjectAccessSpecificationFactory,
            MeshObjectSetFactory                        setFactory,
            ModelBase                                   modelBase,
            AnetMeshBaseLifecycleManager                life,
            NetAccessManager                            accessMgr,
            CachingMap<MeshObjectIdentifier,MeshObject> cache,
            ProxyManager                                proxyManager,
            Context                                     context )
    {
        super(  identifier,
                identifierFactory,
                meshBaseIdentifierFactory,
                netMeshObjectAccessSpecificationFactory,
                setFactory,
                modelBase,
                life,
                accessMgr,
                cache,
                proxyManager,
                context );
    }

    /**
     * Map iterator.
     *
     * @return the iterator
     */
    public final CursorIterator<MeshObject> getIterator()
    {
        return iterator();
    }

    /**
     * Returns a CursorIterator over the content of this MeshBase.
     * 
     * @return a CursorIterator.
     */
    public CursorIterator<MeshObject> iterator()
    {
        // not sure why these type casts are needed, they should not be
        MapCursorIterator.Values<MeshObjectIdentifier,MeshObject> ret = MapCursorIterator.createForValues(
                (HashMap<MeshObjectIdentifier,MeshObject>) theCache,
                MeshObjectIdentifier.class,
                MeshObject.class );
        return ret;
    }

    /**
     * Determine the number of MeshObjects in this MeshBase.
     *
     * @return the number of MeshObjects in this MeshBase
     */
    public int size()
    {
        return theCache.size();
    }
    
    /**
     * Determine the number of MeshObjects in this MeshBase. This redundant method
     * is provided to make life easier for JavaBeans-aware software.
     *
     * @return the number of MeshObjects in this MeshBase
     * @see #size()
     */
    public final int getSize()
    {
        return size();
    }

    /**
     * Factory method for a IterableMeshBaseDifferencer, with this IterableMeshBase
     * being the comparison base.
     *
     * @return the IterableMeshBaseDifferencer
     */
    public IterableNetMeshBaseDifferencer getDifferencer()
    {
        return new IterableNetMeshBaseDifferencer( this );
    }
}
