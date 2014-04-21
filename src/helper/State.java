package helper;

public class State {
	int state;
	
	public State() {
		state = 0;
	}
	
	public State(final int initialState) {
		state = initialState;
	}
	
	public State(State state) {
		this.state = state.getRawState();
	}
	
	public void setMultipleBits(final int bitStates) {
		state |= bitStates;
	}
	
	public void setBit(final int bitPosition) {
		state |= (1 << bitPosition);
	}
	
	public void clearMultipleBits(final int bitStates) {
		state &= ~bitStates;
	}
	
	public void clearBit(final int bitPosition) {
		state &= ~(1 << bitPosition);
	}
	
	public void setState(State newState) {
		state = newState.getRawState();
	}
	
	public boolean equals(State rhs)
	{
		return this.state == rhs.getRawState();
	}
	
	public void shiftRight() {
		state >>= 1;
	}
	
	public void shiftLeft() {
		state <<= 1;
	}
	
	public int getRawState()
	{
		return state;
	}

	public void setState(int newState) {
		state = newState;
	}
}
