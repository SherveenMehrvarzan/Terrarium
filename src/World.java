
public class World {
	private static Tile[][] map = new Tile[25][25];
	private static String season = "spring";
	private static String precipitation = "clear skies";
	private static double precipitationValue = 0;
	private static int time = 0;
	private static int timeScalar = 1;
	private static double temperature = 0;
	private static double temperatureAmplitude = 35;
	private static double temperatureMid = 50;
	private static double temperatureNonConformity = 5;
	private static double humidity = 0.5; //0 - 1
	private static char wind = 'e'; //n, s, w, e
	
	public static void startSimulation() { //Creates a quasi-procedural map.
		time = 0; //Starts on January 1st (Winter)
		for (int r = 0; r < map.length; r++) { //Filling the 2D array with tiles. Nearby Earth increases probability of Earth.
			for (int c = 0; c < map[r].length; c++) {
				double landProb = Math.random();
				landProb -= (Math.abs(r - map.length / 2.0) / (map.length / 2.0)) * 0.6;
				landProb -= (Math.abs(c - map.length / 2.0) / (map.length / 2.0)) * 0.6;
				if (r > 0 && c > 0) {
					if (map[r-1][c] instanceof Earth) landProb += 0.3;
					if (map[r][c-1] instanceof Earth) landProb += 0.3;
				}
				if (r < 3 || r > 22 || c < 3 || c > 22) {
					map[r][c] = new Water(r, c);
				} else if (landProb > 0.30) {
					map[r][c] = new Earth(r, c);
				} else {
					map[r][c] = new Water(r, c);
				}
			}
		}
	}
	
	public static void cycle() { //Runs an update to the world.
		time++;
		shiftClimate();
		shiftWeather();
		generateEvent();
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				map[r][c].cycle();
			}
		}
	}
	
	public static void shiftClimate() { //Shifts the season and wind based off the day.
		int rTime = getRelativeTime();
		if (rTime > 334) { 
			season = "winter";
			wind = 'n';
		} else if (rTime > 243) {
			season = "autumn";
			wind = 'w';
		} else if (rTime > 151) {
			season = "summer";
			wind = 's';
		} else if (rTime > 59) {
			season = "spring";
			wind = 'e';
		} else {
			season = "winter";
			wind = 'n';
		}
	}
	
	public static void shiftWeather() { //Determines random temperature and humidity values, applies to world.
		double hRandom = Math.random();
		int rTime = getRelativeTime();
		
		double midTemp = (rTime / 365.0)*2*Math.PI;
		temperature = temperatureAmplitude*Math.sin(midTemp - (90 / 365.0)*2*Math.PI) + temperatureMid; //Temperature moves in a sine curve.
		
		if (season.equals("winter")) { //Seasonal random changes to temperature and humidity.
			if (rTime > 334 || rTime < 19) {  //Before mid-winter
				temperature += Math.random()*temperatureNonConformity*2 - temperatureNonConformity;
				hRandom *= 1.025;
			} else { //After mid-winter
				temperature += Math.random()*temperatureNonConformity;
				hRandom *= 1.025;
			}
		} else if (season.equals("autumn")) {
			if (rTime > 289) { //After mid-autumn
				temperature += Math.random()*temperatureNonConformity*2 - temperatureNonConformity;
				hRandom *= 1.05;
			} else { //Before mid-autumn
				hRandom *= 1.05;
				temperature -= Math.random()*temperatureNonConformity;
			}
		} else if (season.equals("summer")) {
			if (rTime > 197) { //After mid-summer
				hRandom *= 0.95;
				temperature -= Math.random()*temperatureNonConformity;
			} else { //Before mid-summer
				temperature += Math.random()*temperatureNonConformity;
				hRandom *= 0.95;
			}
		} else {
			if (rTime > 105) { //After mid-spring
				temperature += Math.random()*temperatureNonConformity;
				hRandom *= 1;
			} else { //Before mid-spring
				temperature += Math.random()*temperatureNonConformity*2 - temperatureNonConformity;
				hRandom *= 1.05;
			}
		}
		shiftHumidity(hRandom);
		shiftPrecipitation();
	}
	
	public static void shiftPrecipitation() { //Alters humidity and determines precipitation.
		if (humidity > 0.9) {
			if (temperature > 32) precipitation = "rain storm";
			else precipitation = "snow storm";
			precipitationValue = 1;
		} else if (humidity > 0.75) {
			if (temperature > 32) precipitation = "heavy rain";
			else precipitation = "heavy snow";
			precipitationValue = 0.75;
		} else if (humidity > 0.5) {
			if (temperature > 32) precipitation = "light rain";
			else precipitation = "light snow";
			precipitationValue = 0.50;
		} else if (humidity > 0.25) {
			precipitation = "cloudy";
			precipitationValue = 0.25;
		} else {
			precipitation = "clear skies";
			precipitationValue = 0;
		}
	}
	
	public static void shiftHumidity(double hRandom) { //Alters humidity.
		if (hRandom > 0.9) humidity += 0.1;
		else if (hRandom > 0.75) humidity += 0.07;
		else if (hRandom > 0.5) humidity += 0.05;
		else if (hRandom > 0.25) humidity -= 0.05;
		else if (hRandom > 0.1) humidity -= 0.07;
		else humidity -= 0.1;
		if (humidity < 0) humidity = 0;
		if (humidity > 1) humidity = 1;
	}
	
	public static void generateEvent() { //Generates a random natural disaster.
		if (Math.random() < 0.03) {
			double event = Math.random();
			if (event > 0.75) {
				humidity = 0.95;
				precipitation = "flash flood";
			} else if (event > 0.25) {
				lightningStrike();
			} else fire((int)(Math.random()*map.length), (int)(Math.random()*map.length));
		}
	}
	
	public static void lightningStrike() { //Lightning strike on random tile.
		int strikes = (int)(Math.random()*10 + 1);
		for (int i = 0; i < strikes; i++) {
			int r = (int)(Math.random()*map.length);
			int c = (int)(Math.random()*map[r].length);
			map[r][c].removeGeneralLife((int)(Math.random()*3)); //Removes up to 2 life forms. 
			if (Math.random() > 0.8) fire(r, c); //20% chance of fire after strike.
		}
	}
	
	public static void fire(int r, int c) { //Sets a tile on fire. Can last for up to a month.
		map[r][c].setFireStatus((int)(Math.random()*30 + 1));
	}
	
	public static void shiftWind(char direction) {
		wind = direction;
	}
	
	public static void shiftTimeLapse(int scalar) {
		timeScalar = scalar;
	}	
	
	public static void reset() { //Resets basic world climate and time conditions, leaves land.
		season = "spring";
		precipitation = "clear skies";
		time = 0;
		timeScalar = 1;
		temperature = 60;
		humidity = 0.5;
		wind = 'e';
	}
	
	public static int totalFox() { //Returns total number of foxes.
		int fox = 0;
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				fox += World.getTile(r, c).foxCount();
			}
		}
		return fox;
	}
	
	public static int totalDeer() { //Returns total number of deer.
		int deer = 0;
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				deer += World.getTile(r, c).deerCount();
			}
		}
		return deer;
	}
	
	public static Tile getTile(int r, int c) { //Returns the tile at a given coordinate.
		return map[r][c];
	}
	
	public static int getMapLength() {
		return map.length;
	}
	
	public static void setTile(int r, int c, Tile t) {
		map[r][c] = t;
	}
	
	public static int getRelativeTime() { //Returns the day of the year.
		return time % 365;
	}
	
	public static int getRelativeYear() {
		return time / 365;
	}
	
	public static double getTemperature() {
		return temperature;
	}
	
	public static double getHumidity() {
		return humidity;
	}
	
	public static String getPrecipitation() {
		return precipitation;
	}
	
	public static double getPrecipitationValue() {
		return precipitationValue;
	}
	
	public static String getSeason() {
		return season;
	}
	
}
