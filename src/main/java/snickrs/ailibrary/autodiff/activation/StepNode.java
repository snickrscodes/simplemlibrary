package snickrs.ailibrary.autodiff.activation;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;
import snickrs.ailibrary.autodiff.*;

public class StepNode extends UnaryNode {
	public StepNode(Node left) {
		super(left);
	}
	public INDArray child_evaluate() {
		this.m = Transforms.step(children.get(0).evaluate());
		return this.m;
	}
	public void child_diff(INDArray upstream) {
		partials.set(0, Nd4j.zerosLike(upstream));
	}
}
