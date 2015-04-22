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

package org.infogrid.kernel.net.test.mesh.externalized;

import org.infogrid.testharness.AbstractTestGroup;

/**
 * Tests the externalization of NetMeshObjectIdentifiers and NetMeshBaseIdentifiers.
 */
public abstract class AllTests
        extends
            AbstractTestGroup
{
    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        TestSpec [] tests = {
                new TestSpec( NetSerializerTest1.class ),
                new TestSpec( NetSerializerTest2.class ),
                new TestSpec( NetSerializerTest3.class ),
                new TestSpec( NetSerializerTest4.class ),
                new TestSpec( NetSerializerTest5.class ),
                new TestSpec( NetSerializerTest6.class ),
        };

        runTests( tests );
    }
}
