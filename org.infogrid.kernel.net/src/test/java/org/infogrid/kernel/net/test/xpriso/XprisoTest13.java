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

package org.infogrid.kernel.net.test.xpriso;

import java.util.concurrent.ScheduledExecutorService;
import org.infogrid.mesh.net.NetMeshObject;
import org.infogrid.mesh.net.NetMeshObjectIdentifier;
import org.infogrid.mesh.net.security.AccessLocallyNotPermittedException;
import org.infogrid.meshbase.net.NetMeshBase;
import org.infogrid.meshbase.net.NetMeshBaseIdentifier;
import org.infogrid.meshbase.net.NetMeshBaseLifecycleManager;
import org.infogrid.meshbase.net.NetMeshObjectAccessSpecification;
import org.infogrid.meshbase.net.m.NetMMeshBase;
import org.infogrid.meshbase.net.proxy.m.MPingPongNetMessageEndpointFactory;
import org.infogrid.meshbase.net.security.DelegatingNetAccessManager;
import org.infogrid.meshbase.net.security.NetAccessManager;
import org.infogrid.meshbase.security.PermitAllAccessManager;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.util.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests NetAccessManager protections for accessLocally.
 */
public class XprisoTest13
    extends
        AbstractXprisoTest
{
    /**
     * Run the test.
     *
     * @throws Exception all kinds of things can go wrong in tests
     */
    @Test
    public void run()
        throws
            Exception
    {
        log.info( "Instantiating objects in mb1" );

        NetMeshObjectIdentifier ida = mb1.getMeshObjectIdentifierFactory().fromExternalForm( "#a" );

        Transaction tx1 = mb1.createTransactionAsap();

        NetMeshBaseLifecycleManager life1 = mb1.getMeshBaseLifecycleManager();

        NetMeshObject obja_mb1 = life1.createMeshObject( ida, TestSubjectArea.AA );

        tx1.commitTransaction();

        //
        
        NetMeshObjectAccessSpecification spec = mb1.getNetMeshObjectAccessSpecificationFactory().obtain(
                        mb1.getIdentifier(),
                        ida );
                
        log.info( "Accessing replica at mb2 from mb1, which should work" );

        NetMeshObject obja_mb2 = mb_insecure.accessLocally( spec );
                
        checkObject( obja_mb2, "obja_mb2 not found" );

        //

        log.info( "Accessing replica at mb3 from mb2, which should fail" );

        try {
            NetMeshObject obja_mb3 = mb_secure.accessLocally( spec );

            reportError( "Should have thrown exception" );
            checkObject( obja_mb3, "obja_mb3 unexpectedly found" );

        } catch( AccessLocallyNotPermittedException ex ) {
            // correctly
            checkEqualsInSequence( ex.getFailedAccessSpecifications(), new NetMeshObjectAccessSpecification[] { spec }, "wrong NetMeshObjectAccessSpecification" );
        }
    }

    /**
     * Setup.
     *
     * @throws Exception all kinds of things can go wrong in tests
     */
    @Before
    @Override
    public void setup()
        throws
            Exception
    {
        super.setup();
        
        net1 = theMeshBaseIdentifierFactory.fromExternalForm( "test://one.local" );
        net2 = theMeshBaseIdentifierFactory.fromExternalForm( "test://two.local" );
        net3 = theMeshBaseIdentifierFactory.fromExternalForm( "test://three.local" );

        MPingPongNetMessageEndpointFactory endpointFactory = MPingPongNetMessageEndpointFactory.create( exec );
        endpointFactory.setNameServer( theNameServer );

        mb1         = NetMMeshBase.create( net1, theModelBase, null,                endpointFactory, rootContext );
        mb_insecure = NetMMeshBase.create( net2, theModelBase, null,                endpointFactory, rootContext );
        mb_secure   = NetMMeshBase.create( net3, theModelBase, secureAccessManager, endpointFactory, rootContext );

        theNameServer.put( mb1.getIdentifier(), mb1 );
        theNameServer.put( mb_insecure.getIdentifier(), mb_insecure );
        theNameServer.put( mb_secure.getIdentifier(), mb_secure );
    }

    /**
     * Clean up after the test.
     */
    @After
    public void cleanup()
    {
        mb1.die();
        mb_insecure.die();
        mb_secure.die();
        
        exec.shutdown();
    }

    /**
     * The first NetMeshBaseIdentifier.
     */
    protected NetMeshBaseIdentifier net1;

    /**
     * The second NetMeshBaseIdentifier.
     */
    protected NetMeshBaseIdentifier net2;

    /**
     * The third NetMeshBaseIdentifier.
     */
    protected NetMeshBaseIdentifier net3;

    /**
     * AccessManager for the secure NetMeshBase.
     */
    protected NetAccessManager secureAccessManager = DelegatingNetAccessManager.create( PermitAllAccessManager.create() );

    /**
     * The first NetMeshBase.
     */
    protected NetMeshBase mb1;

    /**
     * The second NetMeshBase.
     */
    protected NetMeshBase mb_insecure;

    /**
     * The third NetMeshBase.
     */
    protected NetMeshBase mb_secure;

    /**
     * Our ThreadPool.
     */
    protected ScheduledExecutorService exec = createThreadPool( 3 ); // gotta have two threads

    // Our Logger
    private static Log log = Log.getLogInstance( XprisoTest13.class );
}
