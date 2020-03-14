package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.FlappyBird;
import com.mygdx.game.sprites.Bird;
import com.mygdx.game.sprites.Tube;

public class PlayState extends State {
    private Texture bg;
    private Bird bird;
    private Tube tube;

    public static final int TUBE_SPACING = 125;
    public static final int TUBE_COUNT =5;
    private Array<Tube> tubes;


    public PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(50, 300);
        bg = new Texture("bg.png");
        camera.setToOrtho(false, FlappyBird.WIDTH/2, FlappyBird.HEIGHT/2);
        tube = new Tube(100);

        tubes = new Array<Tube>();
        for(int i = 0; i<TUBE_COUNT; i++) {
            tubes.add(new Tube(i * TUBE_SPACING + Tube.TUBE_WIDTH));

        }

    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            bird.jump();

        }
    }

    @Override
    public void update(float dt) {
        bird.update(dt);
        handleInput();
        camera.position.x = bird.getPosition().x;

        for (Tube tube : tubes) {
            if(camera.position.x - camera.viewportWidth / 2 > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x + (Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT);
            }
            if(tube.collides(bird.getBounds())){
                gsm.set(new gameover(gsm));
            }
        }
        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, camera.position.x - (camera.viewportWidth/2), 0);
        sb.draw(bird.getBird(), bird.getPosition().x, bird.getPosition().y, bird.getBird().getWidth(), bird.getBird().getHeight());
//        sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y, bird.getBird().getWidth(), bird.getBird().getHeight()*2);
//        sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y, bird.getBird().getWidth(), bird.getBird().getHeight());
        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        sb.end();
    }

    @Override
    public void dispose() {
    }
}
