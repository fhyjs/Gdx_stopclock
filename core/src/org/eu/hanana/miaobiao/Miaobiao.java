package org.eu.hanana.miaobiao;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;

public class Miaobiao extends ApplicationAdapter {
	private final PlatformSpecificCode platformSpecificCode;
	public SpriteBatch batch;
	public Skin skin;
	public Stage stage;
	Label timeclock,bsT,bpT,fps;
	Button btn_start,btn_pause;
	public ClickListener btnscl,btnpcl;
	boolean btn_start_mode,btn_pause_mode;//开始按钮T:开始,F:停止;暂停按钮T:继续,F:暂停
	public Miaobiao(PlatformSpecificCode platformSpecificCode){
		this.platformSpecificCode = platformSpecificCode;
	}
	@Override
	public void create () {
		if (VisUI.isLoaded()) return;
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		Skin visskin = new Skin(Gdx.files.internal("skin/x2/uiskin.json"));
		visskin.getFont("default-font").getData().markupEnabled=true;
		skin.getFont("default-font").getData().markupEnabled=true;
		visskin.getFont("small-font").getData().markupEnabled=true;
		VisUI.load(visskin);
		stage = new Stage();
		Table table = new Table(skin);
		table.setFillParent(true);
		bsT=new Label("",skin);
		fps=new Label("",skin);
		bpT=new Label("",skin);
		fps.setPosition(10,20);
		timeclock=new Label("00:00:00",skin);
		timeclock.setFontScale(4);
		table.add(timeclock).width(Gdx.graphics.getWidth()-20).height(100).pad(20);
		table.row();
		btn_start=new Button(skin);
		btn_start.addListener(btnscl=new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (btn_start.isDisabled()) return;
				if (btn_start_mode) {
					platformSpecificCode.start();
				}else {
					platformSpecificCode.stop();
				}
			}
		});
		btn_pause=new Button(skin);
		btn_pause.addListener(btnpcl=new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (btn_pause.isDisabled()) return;
				if (btn_pause_mode) {
					platformSpecificCode.resume();
				}else {
					platformSpecificCode.pause();
				}
			}
		});
		table.add(btn_start).height(150).width(200).pad(10);
		table.row();
		table.add(btn_pause).height(150).width(300).pad(10);
		btn_start.add(bsT);
		btn_pause.add(bpT);
		bsT.setFontScale(2.7f);
		bpT.setFontScale(2.7f);
		stage.addActor(table);
		Gdx.input.setInputProcessor(new CirnoInputProcessor());
		CirnoInputProcessor.INSTANCE.inputProcessors.add(stage);

		btn_start_mode=true;

		new Thread(this::ticker).start();
	}

	private void ticker() {
		while (true){
			try {
				update();
				Thread.sleep(10);
			} catch (InterruptedException ignored) {
			}
		}
	}

	@Override
	public void render () {
		ScreenUtils.clear(new Color(0x4182a4ff));
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		fps.setText(Gdx.graphics.getFramesPerSecond()+"fps");
		batch.begin();
		fps.draw(batch,1);
		batch.end();
	}
	public void update(){
		if(TimeClock.getTC()!=null) {
			timeclock.setText(TimeClock.getTC().toString());
			btn_start_mode=!TimeClock.getTC().isrunning;
			btn_pause_mode=TimeClock.getTC().ispause;
			btn_start.setDisabled(!TimeClock.getTC().ispause);
			btn_pause.setDisabled(!TimeClock.getTC().isrunning);
		}else {
			btn_start.setDisabled(false);
			btn_pause.setDisabled(true);
		}
		if (btn_start_mode){
			bsT.setText(platformSpecificCode.getStringResource("start"));
		}else {
			bsT.setText(platformSpecificCode.getStringResource("stop"));
		}
		if (btn_pause_mode){
			bpT.setText(platformSpecificCode.getStringResource("resume"));
		}else {
			bpT.setText(platformSpecificCode.getStringResource("pause"));
		}
		btn_pause.setColor(btn_pause.isDisabled()?Color.GRAY:Color.WHITE);
		btn_start.setColor(btn_start.isDisabled()?Color.GRAY:Color.WHITE);
	}
	@Override
	public void dispose () {
		batch.dispose();
		skin.dispose();
		VisUI.dispose();
	}
}
