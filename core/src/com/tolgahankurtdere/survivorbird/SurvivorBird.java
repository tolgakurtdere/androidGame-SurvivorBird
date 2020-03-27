package com.tolgahankurtdere.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

import javax.xml.soap.Text;

public class SurvivorBird extends ApplicationAdapter {
	Preferences prefs;
	int highScore;
	SpriteBatch spriteBatch;
	Texture background;
	Texture bird;
	Texture enemy;
	float birdX,birdY,birdWidth,birdHeight;
	int gameState = 0;
	float velocity = 0, gravity = 0.5f;
	float enemyVelocity = 10;
	Random random;
	int score = 0;
	int enemyScoreIndex = 0;
	BitmapFont bitmapFont1,bitmapFont2;

	int enemyNumber = 4;
	float [] enemyX = new float[enemyNumber];
	float distance = 0;
	float [] enemyY1 = new float[enemyNumber];
	float [] enemyY2 = new float[enemyNumber];
	float [] enemyY3 = new float[enemyNumber];
	float enemyWidth,enemyHeight;

	Circle birdCircle;
	Circle [] enemyCircles1 = new Circle[enemyNumber];
	Circle [] enemyCircles2 = new Circle[enemyNumber];
	Circle [] enemyCircles3 = new Circle[enemyNumber];

	//ShapeRenderer shapeRenderer;

	@Override
	public void create () {
		prefs = Gdx.app.getPreferences("mypref");
		spriteBatch = new SpriteBatch();
		background = new Texture("background.png");
		bird = new Texture("bird.png");
		enemy = new Texture("enemy.png");

		birdX = Gdx.graphics.getWidth() / 5; //necessary initializations
		birdY = Gdx.graphics.getHeight() / 2;
		birdWidth = Gdx.graphics.getWidth() / 15f;
		birdHeight = Gdx.graphics.getHeight() / 10f;
		enemyWidth = Gdx.graphics.getWidth() / 11.5f;
		enemyHeight = Gdx.graphics.getHeight() / 8f;

		distance = Gdx.graphics.getWidth() / 2;
		random = new Random();
		birdCircle = new Circle();

		//shapeRenderer = new ShapeRenderer();
		bitmapFont1 = new BitmapFont();
		bitmapFont1.setColor(Color.WHITE);
		bitmapFont1.getData().setScale(3);

		bitmapFont2 = new BitmapFont();
		bitmapFont2.setColor(Color.BLACK);
		bitmapFont2.getData().setScale(5);

		for(int i=0;i<enemyNumber;i++){
			enemyY1[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));
			enemyY2[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));
			enemyY3[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));

			while(true){ //to avoid overlap of enemies
				if(enemyY1[i] > enemyY2[i]-enemyHeight && enemyY1[i] < enemyY2[i]+enemyHeight){
					enemyY1[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));
					continue;
				}
				if(enemyY1[i] > enemyY3[i]-enemyHeight && enemyY1[i] < enemyY3[i]+enemyHeight){
					enemyY1[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));
					continue;
				}
				if(enemyY2[i] > enemyY3[i]-enemyHeight && enemyY2[i] < enemyY3[i]+enemyHeight){
					enemyY2[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));
					continue;
				}
				break;
			}

			enemyX[i] = Gdx.graphics.getWidth() + (i * distance);

			enemyCircles1[i] = new Circle();
			enemyCircles2[i] = new Circle();
			enemyCircles3[i] = new Circle();
		}
		highScore = prefs.getInteger("highScore");
	}

	@Override
	public void render () {
		spriteBatch.begin();
		spriteBatch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gameState == 0){ //Game is just opened
			if(Gdx.input.justTouched()){
				gameState = 1;
			}
		}

		else if(gameState == 1){ //Game is started

			if(enemyX[enemyScoreIndex] < birdX){
				score++;
				if(enemyScoreIndex < enemyNumber-1){
					enemyScoreIndex++;
				}
				else{
					enemyScoreIndex = 0;
				}
			}

			for(int i=0;i<enemyNumber;i++){

				if(enemyX[i] + enemy.getWidth()/10 < 0){
					enemyY1[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));
					enemyY2[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));
					enemyY3[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));

					while(true){
						if(enemyY1[i] > enemyY2[i]-enemyHeight && enemyY1[i] < enemyY2[i]+enemyHeight){
							enemyY1[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));
							continue;
						}
						if(enemyY1[i] > enemyY3[i]-enemyHeight && enemyY1[i] < enemyY3[i]+enemyHeight){
							enemyY1[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));
							continue;
						}
						if(enemyY2[i] > enemyY3[i]-enemyHeight && enemyY2[i] < enemyY3[i]+enemyHeight){
							enemyY2[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));
							continue;
						}
						break;
					}

					enemyX[i] += enemyNumber * distance;
				}
				enemyX[i] -= enemyVelocity;

				spriteBatch.draw(enemy,enemyX[i],enemyY1[i],enemyWidth,enemyHeight);
				spriteBatch.draw(enemy,enemyX[i],enemyY2[i],enemyWidth,enemyHeight);
				spriteBatch.draw(enemy,enemyX[i],enemyY3[i],enemyWidth,enemyHeight);

				enemyCircles1[i] = new Circle(enemyX[i]+enemyWidth/2,enemyY1[i]+enemyHeight/2,enemyHeight/2);
				enemyCircles2[i] = new Circle(enemyX[i]+enemyWidth/2,enemyY2[i]+enemyHeight/2,enemyHeight/2);
				enemyCircles3[i] = new Circle(enemyX[i]+enemyWidth/2,enemyY3[i]+enemyHeight/2,enemyHeight/2);
			}

			if (score > highScore) { //to arrange highScore
				highScore = score;
				prefs.putInteger("highScore", highScore);
				prefs.flush();
			}

			if(birdY > 0 && birdY < (Gdx.graphics.getHeight()-birdHeight)){ //check bottom and top borders
				velocity += gravity;
				birdY -= velocity;

				if(Gdx.input.justTouched()){
					velocity = -12;
				}
			}
			else{
				gameState = 2;
			}
		}

		else if(gameState == 2){ //Game is over
			bitmapFont2.draw(spriteBatch,"Game Over Loser! Tap To Lose Again!",Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight()/2);

			if(Gdx.input.justTouched()){ //restart the game
				gameState = 1;

				velocity = 0;
				score = 0;
				enemyScoreIndex = 0;
				birdX = Gdx.graphics.getWidth() / 5; //necessary initializations again to restart
				birdY = Gdx.graphics.getHeight() / 2;

				for(int i=0;i<enemyNumber;i++){
					enemyY1[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));
					enemyY2[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));
					enemyY3[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));

					while(true){
						if(enemyY1[i] > enemyY2[i]-enemyHeight && enemyY1[i] < enemyY2[i]+enemyHeight){
							enemyY1[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));
							continue;
						}
						if(enemyY1[i] > enemyY3[i]-enemyHeight && enemyY1[i] < enemyY3[i]+enemyHeight){
							enemyY1[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));
							continue;
						}
						if(enemyY2[i] > enemyY3[i]-enemyHeight && enemyY2[i] < enemyY3[i]+enemyHeight){
							enemyY2[i] = random.nextInt((int) (Gdx.graphics.getHeight() - enemyHeight));
							continue;
						}
						break;
					}

					enemyX[i] = Gdx.graphics.getWidth() + (i * distance);

					enemyCircles1[i] = new Circle();
					enemyCircles2[i] = new Circle();
					enemyCircles3[i] = new Circle();
				}

			}
		}


		spriteBatch.draw(bird,birdX,birdY,birdWidth,birdHeight);

		bitmapFont1.draw(spriteBatch,"Score: " + String.valueOf(score),15,70);
		bitmapFont1.draw(spriteBatch,"High Score: " + String.valueOf(highScore),15,Gdx.graphics.getHeight()-50);
		spriteBatch.end();

		birdCircle.set(birdX+birdWidth/2,birdY+birdHeight/2,birdWidth/2);

		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);*/
		for(int i=0;i<enemyNumber;i++){
			/*shapeRenderer.circle(enemyCircles1[i].x,enemyCircles1[i].y,enemyCircles1[i].radius);
			shapeRenderer.circle(enemyCircles2[i].x,enemyCircles2[i].y,enemyCircles2[i].radius);
			shapeRenderer.circle(enemyCircles3[i].x,enemyCircles3[i].y,enemyCircles3[i].radius);*/

			if(Intersector.overlaps(birdCircle,enemyCircles1[i]) || Intersector.overlaps(birdCircle,enemyCircles2[i]) || Intersector.overlaps(birdCircle,enemyCircles3[i])){
				gameState = 2;
			}
		}
		//shapeRenderer.end();

	}
	
	@Override
	public void dispose () {

	}

}
