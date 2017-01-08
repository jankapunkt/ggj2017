package com.jankuester.ggj.twentyseventeen.models.items;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.collision.btShapeHull;

public class G3DBModelUtilsUtils {

	/** 
	 * found on https://github.com/libgdx/libgdx/blob/master/tests/gdx-tests/src/com/badlogic/gdx/tests/bullet/ConvexHullTest.java#L55
	 * @param model your model
	 * @param optimize for better hull shape, if simple is not precice enough
	 * @return convex hull shape of your model
	 */
	public static btConvexHullShape createConvexHullShape (final Model model, boolean optimize) {
		final Mesh mesh = model.meshes.get(0);
		final btConvexHullShape shape = new btConvexHullShape(mesh.getVerticesBuffer(), mesh.getNumVertices(), mesh.getVertexSize());
		if (!optimize) return shape;
		// now optimize the shape
		final btShapeHull hull = new btShapeHull(shape);
		hull.buildHull(shape.getMargin());
		final btConvexHullShape result = new btConvexHullShape(hull);
		// delete the temporary shape
		shape.dispose();
		hull.dispose();
		return result;
	}
}
