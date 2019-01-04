package group03;
import robocode.*;
import java.util.*;
import robocode.util.Utils;
//import java.awt.Color;

public class G03_Leader extends TeamRobot
{
/*
	 * run: Droideka's default behavior
	 */
	public int flag = 0;
	int turnDirection = 1;
	final double PI = Math.PI;

	LinerEnemyRad target = new LinerEnemyRad();
	Hashtable targets = new Hashtable();

	Direction movDir = new Direction();
	Direction sweepDir = new Direction();
	Lib help = new Lib();

	double firePower;
	double sweepKaku = PI;

	public void run() {

		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
        
        turnRadarRightRadians(2*PI);
		while(true) {
		    if(flag == 0){
			//turnGunRight(360);
			if(getRadarTurnRemaining() == 0){
                  setTurnRadarRight(360);
				  }
			execute();	   
			}
			else if(flag == 1) {
            doAntiGravMove();
			doScanner();
			doGun();
			if(firePower != 0) fire(firePower);
			execute();
			}
			
		}
	}

void doAntiGravMove() {
   		double xforce = 0;
	    double yforce = 0;
	    double force;
	    double ang;
	    GravPoint p;
		Enemy en;
    	Enumeration e = targets.elements();

	
		while (e.hasMoreElements()) {
    	    en = (Enemy)e.nextElement();
			if (en.live) {
				final double ENEMYFORCE = -1000;
			
				p = new GravPoint(en.x,en.y, ENEMYFORCE);

		        force = p.power/Math.pow(help.getRange(getX(),getY(),p.x,p.y),2);

		        ang = help.normalAngle(PI/2 - Math.atan2(getY() - p.y, getX() - p.x)); 
		 
		        xforce += Math.sin(ang) * force;
		        yforce += Math.cos(ang) * force;
			}
	    }

	
		final double WALLFORCE = 5000;
	    xforce += WALLFORCE/Math.pow(help.getRange(getX(), getY(), getBattleFieldWidth(), getY()), 3);
	    xforce -= WALLFORCE/Math.pow(help.getRange(getX(), getY(), 0, getY()), 3);
	    yforce += WALLFORCE/Math.pow(help.getRange(getX(), getY(), getX(), getBattleFieldHeight()), 3);
	    yforce -= WALLFORCE/Math.pow(help.getRange(getX(), getY(), getX(), 0), 3);
	    
		
		double kaku = getHeadingRadians() + Math.atan2(yforce, xforce) - PI/2;

		int dir;
		if (kaku > PI/2) {
	        kaku -= PI;
	        dir = -1;
	    }
	    else if (kaku < -PI/2) {
	        kaku += PI;
	        dir = -1;
	    }
	    else {
	        dir = 1;
	    }


	    double changeInEnergy = target.previousEnergy - target.energy;
	    if (changeInEnergy != 0) {
	
	    	double moveDistance;
		if (target.distance > 400) {
			moveDistance = 100;
		} else {
			moveDistance = 300;
		}
	    setTurnRightRadians(kaku);
	    setAhead(moveDistance * dir);
	    }

	}

			
	void doScanner() {
		setTurnRadarLeftRadians(2*PI);
	}

	void doGun() {
		firePower = 0;

		if (target.setNextXY(getX(), getY()) == true) {
			double nextX = target.nextX - getX();
			double nextY = target.nextY - getY();
			double kaiten = PI/2 - Math.atan2(nextY, nextX);
			setTurnGunRightRadians(help.normalAngle(kaiten - getGunHeadingRadians()));
					
			if (target.nextX > 0 
				&& target.nextY > 0 
				&& target.nextX < getBattleFieldWidth() 
				&& target.nextY < getBattleFieldHeight()) {
					firePower = 1;
			}
		}
	}
	
	/*
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		if(flag == 0) {
		  String enemyname = e.getName();
		  if(!isTeammate(e.getName()) ){
            if(e.getBearing() >= 0){
		        turnDirection = 1;
		    } else {
		    	turnDirection = -1;
		    }
			
			if(enemyname.contains("Leader")){
			
           /* smartFire(e.getDistance());
			turnRight(e.getBearing());	
			setAhead(e.getDistance() + 5);
			scan();*/
			double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
			setTurnRadarRightRadians(2.0 * Utils.normalRelativeAngle(absoluteBearing - getRadarHeadingRadians()));
			setTurnGunRightRadians(Utils.normalRelativeAngle(absoluteBearing + Math.asin(e.getVelocity() / Rules.getBulletSpeed(3) * Math.sin(e.getHeadingRadians() - absoluteBearing)) - getGunHeadingRadians()));
	        smartFire(e.getDistance());
			setMaxVelocity(Math.abs(e.getBearing()) > 30 ? 4 : 8);
		    setTurnRight(e.getBearing());	
		    setAhead(e.getDistance());
		  }
		} 
     }else {
		
if(!isTeammate(e.getName())){
        double absbearing_rad = (getHeadingRadians()+e.getBearingRadians())%(2*PI);

		LinerEnemyRad en;
		if (targets.containsKey(e.getName())) {
			en = (LinerEnemyRad)targets.get(e.getName());
		} else {
			en = new LinerEnemyRad();
			targets.put(e.getName(),en);
		}

	    en.name = e.getName();
		en.x = getX()+Math.sin(absbearing_rad)*e.getDistance();
		en.y = getY()+Math.cos(absbearing_rad)*e.getDistance();
		en.bearing = e.getBearingRadians();
		en.head = e.getHeadingRadians();
		en.checkTime = getTime();
		en.speed = e.getVelocity();
		en.distance = e.getDistance();

		en.live = true;
		if ((en.distance < target.distance)||(target.live == false)) {
			target = en;
		}

		en.setEnergy(e.getEnergy());
	    
	}  // isteam
	} //else if
}
	public void onHitByBullet(HitByBulletEvent e) {
		if(flag == 1) turnRadarRight(getHeading() + e.getBearing() - getRadarHeading());
	}

	public void onRobotDeath(RobotDeathEvent e) {
		String deathRobotName = e.getName();
		if(deathRobotName.contains("Leader")){
          flag = 1;
		  }
		if(flag == 1){
        Enemy en = (Enemy)targets.get(e.getName());
		en.live = false;
		}
	}
	
    public void onHitWall(HitWallEvent e) {
		if(flag == 0) {
        turnLeft(120);
		setAhead(500);
		}
	}
	
    public void onHitRobot(HitRobotEvent e){

		if(flag == 0 && !isTeammate(e.getName())){
		
	    	if(e.getBearing() >= 0){
			    turnDirection = 1;
			} else {
				turnDirection = -1;
			}
			
        	turnRight(e.getBearing());
			
 			if (e.getEnergy() > 16) {
				fire(3);
			} else if (e.getEnergy() > 10) {
				fire(2);
			} else if (e.getEnergy() > 4)  {
				fire(1);
			} else if (e.getEnergy() > 2)  {
				fire(.5);
			} else if (e.getEnergy() > .4) {
				fire(.1);
			}
			ahead(40);
		}
	}
	
    public void onBulletHit(BulletHitEvent e){
      if(flag == 0 && !isTeammate(e.getName()) && (e.getName()).contains("Leader")){
       fire(3);
	   }
	} 
	
    public void smartFire(double robotDistance) {
		if (robotDistance > 200 || getEnergy() < 15) {
			setFire(1);
		} else if (robotDistance > 50) {
			setFire(2);
		} else {
			setFire(3);
		}
	}

}
