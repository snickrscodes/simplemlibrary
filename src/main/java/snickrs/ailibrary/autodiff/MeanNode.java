package snickrs.ailibrary.autodiff;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class MeanNode extends UnaryNode {
	public int[] axes;
	public boolean keepdims;
	public MeanNode(Node left, boolean keepdims, int ... axes) {
		super(left);
		this.axes = axes;
		this.keepdims = keepdims;
	}
	public MeanNode(Node left, int ... axes) {
		super(left);
		this.axes = axes;
		this.keepdims = true;
	}
	public MeanNode(Node left) {
		super(left);
		this.axes = new int[] {-1};
		this.keepdims = true;
	}
	public INDArray child_evaluate() {
		this.m = children.get(0).evaluate().mean(keepdims, axes);
		return this.m;		
	}
    public long[] shape() {
        long[] shape = children.get(0).shape().clone();
        for (int axis : axes) {
            shape[axis < 0 ? shape.length + axis : axis] = 1;
        }
        return shape;
    }
	public void child_diff(INDArray upstream) {
		long[] shape = children.get(0).m.shape().clone();
		double len = 1;
		for(int axis : axes) {
			int idx = axis < 0 ? shape.length + axis : axis;
			len *= shape[idx];
			shape[idx] = 1;
		}
		partials.set(0, upstream.reshape(shape).add(Nd4j.zeros(shape)).div(len));
	}
}
