package org.apache.commons.graph.coloring;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.graph.Edge;
import org.apache.commons.graph.UndirectedGraph;
import org.apache.commons.graph.Vertex;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * Contains the graph coloring implementation. http://scienceblogs.com/goodmath/2007/06/graph_coloring_algorithms_1.php
 */
public final class GraphColoring
{

    /**
     * Colors the graph such that no two adjacent vertices share the same color.
     *
     * @param g The graph.
     * @return The color - vertex association.
     */
    public static <V extends Vertex, E extends Edge> ColoredVertices<V> coloring( UndirectedGraph<V, E> g )
    {
        ColoredVertices<V> coloredVertices = new ColoredVertices<V>();
        List<V> uncoloredVertices = null;

        // decreasing sorting all vertices by degree.
        Map<Integer, List<V>> orderedVertex = new TreeMap<Integer, List<V>>( new Comparator<Integer>()
        {
            public int compare( Integer o1, Integer o2 )
            {
                return o2.compareTo( o1 );
            }
        } );

        for ( V v : g.getVertices() )
        {
            int degree = g.getDegree( v );
            List<V> vertices = orderedVertex.get( degree );

            if ( vertices == null )
            {
                vertices = new ArrayList<V>();
            }

            vertices.add( v );
            orderedVertex.put( degree, vertices );
        }

        uncoloredVertices = new ArrayList<V>();
        for ( Integer key : orderedVertex.keySet() )
        {
            uncoloredVertices.addAll( orderedVertex.get( key ) );
        }

        // search coloring
        int currrentColorIndex = 0;

        Iterator<V> it = uncoloredVertices.iterator();
        while ( it.hasNext() )
        {
            V v = it.next();

            // remove vertex from uncolored list.
            it.remove();
            coloredVertices.addColor( v, currrentColorIndex );

            // color all vertices not adjacent
            Iterator<V> it2 = uncoloredVertices.iterator();
            while ( it2.hasNext() )
            {
                V v2 = it2.next();
                if ( g.getEdge( v, v2 ) == null )
                {
                    // v2 is not connect to v.
                    it2.remove();
                    coloredVertices.addColor( v2, currrentColorIndex );
                }
            }

            it = uncoloredVertices.iterator();
            currrentColorIndex++;
        }

        return coloredVertices;
    }

    /**
     * This class can not be instantiated.
     */
    private GraphColoring()
    {
        // do nothing
    }

}
