	import java.util.*;

public abstract class Tile { //Set to private - alter subtiles to accessor/mutators
	String type;
	String color;
	String precipitation;
	String status;
	double waterConcentration;
	double heat;
	ArrayList<Life> lifeforms;
	int maxLife;
	int goldilocksTime;
	int goldilocksVariance;
	int r;
	int c;
	int fireStatus = 0;
	
	//PASS GOLDILOCKS VARIANCE
	
	public Tile(int newR, int newC) { //Constructor.
		type = "empty";
		color = "white";
		precipitation = "empty";
		status = "normal";
		waterConcentration = 0;
		heat = 0;
		lifeforms = new ArrayList<Life>();
		maxLife = 0;
		goldilocksTime = 0;
		goldilocksVariance = (int)(Math.random()*30 - 15);
		r = newR;
		c = newC;
	}
	
	public Tile(int newR, int newC, String newType, String newColor, int newMaxLife) { //Constructor.
		type = newType;
		color = newColor;
		maxLife = newMaxLife;
		precipitation = World.getPrecipitation();
		status = "normal";
		waterConcentration = 0.5;
		heat = 60;
		lifeforms = new ArrayList<Life>();
		goldilocksTime = 0;
		r = newR;
		c = newC;
		goldilocksVariance = (int)(Math.random()*30 - 15);
	}
	
	public Tile(int newR, int newC, double newWaterConcentration, double newHeat, String newType, String newColor, int newMaxLife) { //Constructor 
		type = newType;
		color = newColor;
		maxLife = newMaxLife;
		precipitation = World.getPrecipitation();
		status = "normal";
		waterConcentration = newWaterConcentration;
		heat = newHeat;
		lifeforms = new ArrayList<Life>();
		goldilocksTime = 0;
		r = newR;
		c = newC;
		goldilocksVariance = (int)(Math.random()*30 - 15);
	}
	
	public Tile(int newR, int newC, double newWaterConcentration, double newHeat, String newType, String newColor, int newMaxLife, ArrayList<Life> newLife) { //Constructor
		type = newType;
		color = newColor;
		maxLife = newMaxLife;
		precipitation = World.getPrecipitation();
		status = "normal";
		waterConcentration = newWaterConcentration;
		heat = newHeat;
		lifeforms = newLife;
		goldilocksTime = 0;
		r = newR;
		c = newC;
		goldilocksVariance = (int)(Math.random()*30 - 15);
	}
	
	public abstract void cycle(); //Called every time time changes.
	
	public abstract boolean goldilocksCheck(); //Determines how well land is doing.
	
	public abstract int calcMaxLife(); //Calculates max life.
	
	public Tile[] proximityCheck() { //Returns an array of adjacent tiles (inclusive).
		Tile[] nearby = new Tile[9];
		int counter = 0;
		for (int y = r-1; y <= r+1; y++) {
			for (int x = c-1; x <= c+1; x++) {
				nearby[counter] = World.getTile(y,x);
				counter++;
			}
		}
		return nearby;
	}
	
	public void removeGeneralLife(int num) { //Removes given amount of life.
		while (lifeforms.size() > 0 && num > 0) {
			lifeforms.remove(lifeforms.size()-1);
			num--;
		}
	}
	
	public void removeLife(int index) { //Removes the given life form.
		if (lifeforms.size() > index) {
			lifeforms.remove(index);
			shiftIndex(index);
		}
	}
	
	public void shiftIndex(int index) { //Shifts the arraylist indexes above the index.
		for (int i = index; i < lifeforms.size(); i++) {
			lifeforms.get(i).lowerIndex();
		}
	}
	
	public Deer removeDeer() { //Removes a deer. Returns the removed deer or a default deer if there are no deer.
		int counter = 0;
		Deer removed = new Deer(r, c);
		while (counter < lifeforms.size() && !(lifeforms.get(counter) instanceof Deer)) {
			counter++;
		}
		if (lifeforms.get(counter) instanceof Deer) {
			removed = (Deer) lifeforms.remove(counter);
			shiftIndex(counter);
		}
		return removed;
	}
	
	public void addDeer(int num) { //Adds the given number of deer.
		for (int i = 0; i < num; i++) {
			if (!atLifeCapacity()) lifeforms.add(new Deer(r, c));
		}
	}
	
	public void addFox(int num) { //Adds the given number of fox.
		for (int i = 0; i < num; i++) {
			if (!atLifeCapacity()) lifeforms.add(new Fox(r, c));
		}
	}
	
	public void addLife(Life l) { //Adds the given life form.
		if (!atLifeCapacity()) lifeforms.add(l);
	}
	
	public boolean atLifeCapacity() { //Determines if more life can be added.
		return (lifeforms.size() >= maxLife);
	}
	
	public int deerCount() { //Returns amount of deer on the tile.
		int deer = 0;
		for (int i = 0; i < lifeforms.size(); i++) {
			if (lifeforms.get(i) instanceof Deer) deer++;
		}
		return deer;
	}
	
	public int foxCount() { //Maybe something wrong with this?
		int fox = 0;
		for (int i = 0; i < lifeforms.size(); i++) {
			if (lifeforms.get(i) instanceof Fox) fox++;
		}
		return fox;
	}
	
	public void lifeformCycles() { //Performs the cycle for each life form.
		if (lifeforms.size() > 0) {
			for (int i = 0; i < lifeforms.size(); i++) {
				lifeforms.get(i).cycle();
			}
		}
	}
	
	public void shiftWaterConcentration() { //Alters a tile's water concentration. CHANGE TO ADDITION!
		double shift = 1; //Default as no change in concentration.
		if (World.getPrecipitationValue() == 1) shift = Math.random()*0.50 + 1.50; //Storm.
		else if (World.getPrecipitationValue() == 0.75) shift = Math.random()*0.50 + 1; //Heavy rain.
		else if (World.getPrecipitationValue() == 0.50) shift = Math.random()*0.20 + 1; //Light rain.
		else { //No rain.
			if (World.getTemperature() > 100) shift = Math.random()*0.20 + 0.80;		
			else if (World.getTemperature() > 75) shift = Math.random()*0.10 + 0.90;
		}
		Tile[] nearby = proximityCheck();
		for (int i = 0; i < nearby.length; i++) { //More shift with nearby water.
			if (nearby[i] instanceof Water) shift += 0.03;
		}
		waterConcentration = waterConcentration*shift; //Alter current concentration by shift.
		if (waterConcentration > 1) waterConcentration = 1; //Cutoff.
		if (waterConcentration < 0) waterConcentration = 0.01;
	}
	
	public void shiftWaterConcentration(double num) {
		waterConcentration *= Math.abs(num);
		if (waterConcentration > 1) waterConcentration = 1;
		if (waterConcentration < 0) waterConcentration = 0.01;
	}
	
	public void shiftHeat() { //Alters a tile's local heat.
		if (!status.equals("fire")) {
			if (World.getTemperature() > heat) {
				heat += Math.random()*(World.getTemperature() - heat) * (1 - waterConcentration / 2); //Heat shift based off water concentration.
			} else if (World.getTemperature() < heat) {
				heat -= Math.random()*(heat - World.getTemperature()) * (1 - waterConcentration / 2);
			}
		}
	}
	
	public void shiftStatus() { //Shifts local precipitation, max life, fire status, other weather status
		if (fireStatus > 0) {
			status = "fire";
		} else if (World.getPrecipitationValue() >= 0.5 && World.getTemperature() < 32) {
			status = "snowy";
		} else status = "normal";
		if (World.getPrecipitationValue() == 1) fireStatus -= 3;
		else if (World.getPrecipitationValue() == 0.75) fireStatus -= 2;
		else if (World.getPrecipitationValue() == 0.5) fireStatus -= 1;
		precipitation = World.getPrecipitation();
		maxLife = calcMaxLife();
	}
	
	public void shiftHeat(int num) {
		heat += num;
	}
	
	public void toEarth() { //Turns the tile to earth.
		Tile e = new Earth(r, c, waterConcentration, heat, lifeforms);
		World.setTile(r, c, e);
	}
	
	public void setFireStatus(int num) { //Sets a tile's fire status.
		fireStatus = num;
		if (this instanceof Water) fireStatus = 0;
	}
	
	public void burn() { //Checks if the tile is on fire and then burns appropriately.
		if (fireStatus > 0) {
			Tile[] nearby = proximityCheck(); //Fire spread. Affected by fire status and adjacent tile water concentration.
			for (int i = 0; i < nearby.length; i++) {
				if (!(nearby[i] instanceof Water) && !(nearby[i].getStatus().equals("fire"))) { 
					if ((Math.random() + (fireStatus / 100) - (nearby[i].getWaterConcentration() / 3)) > 0.5) {
						World.getTile(nearby[i].getR(), nearby[i].getC()).setFireStatus((int)(Math.random()*fireStatus));
					}
				}
			}
			if (heat < 900) {
				heat += Math.random()*100 + 50; //Increase heat.
				waterConcentration *= 0.5; //Decrease water concentration.
				removeGeneralLife((int)(Math.random()*lifeforms.size() + 1)); //Kill life.
			}
			goldilocksTime -= 10;
		}
		if (fireStatus > 0) fireStatus--;
	}
	
	public String getType() {
		return type;
	}
	
	public String getColor() {
		return color;
	}
	
	public String getPrecipitation() {
		return precipitation;
	}
	
	public double getWaterConcentration() {
		return waterConcentration;
	}
	
	public double getHeat() {
		return heat;
	}
	
	public int getMaxLife() {
		return maxLife;
	}
	
	public int getGoldilocksTime() {
		return goldilocksTime;
	}
	
	public int getR() {
		return r;
	}
	
	public int getC() {
		return c;
	}
	
	public String getStatus() {
		return status;
	}
	
	public int getTotalLife() {
		return lifeforms.size();
	}
	
}
