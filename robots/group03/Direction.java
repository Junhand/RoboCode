package group03;

/*
 * 方向を制御するためのフラグクラス +1 or -1
 */

class Direction {
	int flag;	// 方向フラグ +1 or -1

	//コンストラクタ
	public Direction() {
		this.flag = 1;
	}
	
	//読み出し
	public int getDir() {
		return this.flag;
	}

	//方向を反転
	public void flip() {
		this.flag *= -1;
	}
}


