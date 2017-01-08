package com.jankuester.ggj.twentyseventeen.models.items.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public class WeaponState implements Disposable {

    public int power;
    public int ammo;
    public int maxammo;
    public int magazine_size;

    private Sound shootSound;
    private Sound emptySound;
    private Sound reloadSound;
    
    public String id;

    public final Texture image;

    public WeaponState(String imagePath) {
	power = 0;
	ammo = 0;
	maxammo = 0;
	magazine_size = 0;
	id = imagePath;
	image = new Texture(Gdx.files.internal(imagePath));
	//shootSound =  Gdx.audio.newSound(Gdx.files.internal(shootSoundPath));
	//emptySound =  Gdx.audio.newSound(Gdx.files.internal(reloadSoundPath));
	//reloadSound =  Gdx.audio.newSound(Gdx.files.internal(emptySoundPath));
	
    }

    public void dispose() {
	image.dispose();
    }

}
