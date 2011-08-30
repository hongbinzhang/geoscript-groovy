package geoscript.workspace

import org.junit.Test
import static org.junit.Assert.*
import geoscript.layer.Layer
import geoscript.feature.Field
import geoscript.feature.Schema

/**
 * The Property Workspace UnitTest
 * @author Jared Erickson
 */
class PropertyTestCase {

    @Test void read() {

        File dir = new File(getClass().getClassLoader().getResource("points.properties").toURI()).parentFile
        assertNotNull(dir)

        Property property = new Property(dir)
        assertNotNull(property)
        assertEquals "Property", property.format
        assertEquals "Property[${dir}/]".toString(), property.toString()

        assertEquals 1, property.layers.size()
        assertEquals "[points]", property.layers.toString()

        Layer layer = property.get("points")
        assertNotNull(layer)
        assertEquals 4, layer.count

        def features = layer.features

        assertEquals "point 1", features[0].get("name")
        assertEquals "point 2", features[1].get("name")
        assertEquals "point 3", features[2].get("name")
        assertEquals "point 4", features[3].get("name")

        assertEquals "POINT (0 0)", features[0].geom.wkt
        assertEquals "POINT (10 10)", features[1].geom.wkt
        assertEquals "POINT (20 20)", features[2].geom.wkt
        assertEquals "POINT (30 30)", features[3].geom.wkt
    }

    @Test void create() {
        File dir = new File(System.getProperty("java.io.tmpdir"))
        Property property = new Property(dir)
        Layer layer = property.create("points", [new Field("geom","Point","EPSG:4326")])
        assertNotNull(layer)
        assertTrue(new File(dir,"points.properties").exists())

        Layer layer2 = property.create(new Schema("lines", [new Field("geom","Point","EPSG:4326")]))
        assertNotNull(layer2)
        assertTrue(new File(dir,"lines.properties").exists())
    }

    @Test void add() {
        File shpDir = new File(getClass().getClassLoader().getResource("states.shp").toURI()).parentFile
        Directory directory = new Directory(shpDir)
        Layer statesLayer = directory.get("states")

        File tempDir = new File(System.getProperty("java.io.tmpdir"))
        Property property = new Property(tempDir)
        Layer propLayer = property.add(statesLayer, "states")
        assertTrue(new File(tempDir,"states.properties").exists())
        assertEquals statesLayer.count, propLayer.count
    }

}