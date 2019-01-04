package group03;

class LinerEnemyRad extends Enemy {
	final double PI = Math.PI;		//ラジアン用に円周率を定数化
	//敵の位置を線形予測
	//エネルギー弾の到達予測から、敵の位置を求める
	public boolean setNextXY(double x0, double y0) {

		boolean flag;
		double dX = x - x0;
		double dY = y - y0;

		double targetOwnHeading = PI/2 - head;
		double vX = Math.cos(targetOwnHeading) * speed;		//敵の進路ベクトル x成分
		double vY = Math.sin(targetOwnHeading) * speed;		//敵の進路ベクトル y成分

		double A = vX * vX + vY * vY - 289;					// Ax^2+Bx+C = 0
		double B = 2 * vX * dX + 2 * vY * dY;
		double C = dX * dX + dY * dY;

		double t1,t2;
		if ( B * B > 4 * A * C) {
			t1 = (- B - Math.sqrt(B * B - 4 * A * C)) / (2 * A); //解の公式
			t2 = (- B + Math.sqrt(B * B - 4 * A * C)) / (2 * A);
			if (t1 < 0) {t1 = t2 + 1;}
			if (t2 < 0) {t2 = t1 + 1;}
			if (t1 > t2) {t1 = t2;} 	//0以上で小さいほうの値を使う

			nextX = x + vX * t1;
			nextY = y + vY * t1;	
			flag = true;	
		} else {
			flag = false;
		}
		return flag;
	}
}


