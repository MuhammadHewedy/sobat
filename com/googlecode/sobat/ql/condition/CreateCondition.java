package com.googlecode.sobat.ql.condition;

public class CreateCondition {
	public enum Type {
		Equals, Greater, Less, Like, ILike;
	}
	static Condition create(Type type, String property, Object value) {
		Condition condition = null;
		switch (type) {
			case Equals:
				condition = new EqualsCondition(property, value);
				break;
			case Greater:
				condition = new GreaterCondition(property, value);
				break;
			case Less:
				condition = new LessCondition(property, value);
				break;
			case Like:
				condition = new LikeCondition(property, value);
				break;
			case ILike:
				condition = new ILikeCondition(property, value);
				break;
			default:
				break;
		}
		return condition;
	}
}
