package group03;


/*
 * 敵ロボットの情報を管理するクラス
 */

class Enemy {
	String name;
	public boolean live; 
	public double bearing;
	public double head;
	public long checkTime; 	//game time that the scan was produced
	public double speed;
	public double x,y;
	public double distance;
	public double energy, previousEnergy;
	public double nextX, nextY;

	//コンストラクタ
	Enemy() {
		distance = 100000;
		previousEnergy = 100;
	}

	//ロボットの予測地点を設定する
	public void setNextXY() {
		nextX = x;
		nextY = y;
	}

	//現在とひとつ前のエネルギーレベルを記録する
	public void setEnergy(double engy) {
		previousEnergy = energy;
		energy = engy;
	}
}

