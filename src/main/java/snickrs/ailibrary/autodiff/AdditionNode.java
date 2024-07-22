package snickrs.ailibrary.autodiff;

import java.util.ArrayList;
import java.util.List;
import org.nd4j.linalg.api.ndarray.INDArray;

public class AdditionNode extends Node {
	public AdditionNode(Node ... children) {
		super(children);
	}
	public AdditionNode(List<Node> children) {
		super(children);
	}
	public INDArray child_evaluate() {
		ArrayList<INDArray> evals = get_evals();
		if(children.size() > 2) {
			this.m = evals.get(0);
			for(int i = 1; i < evals.size(); i++) {
				this.m = this.m.add(evals.get(i));
			}
		} else {
			this.m = children.get(0).m.add(children.get(1).m);	
		}
		return this.m;
	}
	public void child_diff(INDArray upstream) {
		for(int i = 0; i < children.size(); i++) {
			partials.set(i, unbroadcast(children.get(i).m, upstream));
		}
	}
    public long[] shape() {
        long[] resultShape = children.get(0).shape();
        for (int i = 1; i < children.size(); i++) {
            long[] shape = children.get(i).shape();
            resultShape = broadcast(resultShape, shape);
        }
        return resultShape;
    }
}
