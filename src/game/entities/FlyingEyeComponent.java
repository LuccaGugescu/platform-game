package game.entities;

import static com.almasb.fxgl.dsl.FXGL.image;

import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.scene.image.Image;
import javafx.util.Duration;

public class FlyingEyeComponent extends EnemyComponent{
	public FlyingEyeComponent() {
		Image image = image("enemies/Flying eye/Flight.png");
		Image imgWalk = image("enemies/Flying eye/Flight.png");
		Image imgAttack = image("enemies/Flying eye/Attack.png");
		Image imgHit = image("enemies/Flying eye/Take Hit.png");
		Image imgDeath = image("enemies/Flying eye/Death.png");
		animIdle = new AnimationChannel(image, 4, 45, 51, Duration.seconds(0.8), 0, 3);
		animAttack = new AnimationChannel(imgAttack, 8, 45, 57, Duration.seconds(1.5), 0, 7);
		animWalk = new AnimationChannel(imgWalk, 8, 43, 28, Duration.seconds(0.8), 0, 7);
		animHit = new AnimationChannel(imgHit, 4, 41, 33, Duration.seconds(0.45), 0, 3);
		animDeath = new AnimationChannel(imgDeath, 4, 56, 31, Duration.seconds(0.6), 0, 3);
		texture = new AnimatedTexture(animIdle);
		texture.loop();
		texture.setOnCycleFinished(() -> {
			if (texture.getAnimationChannel() == animHit) {
				this.isGettingHit = false;
				this.speed = -75;
				texture.loopAnimationChannel(animWalk);
			}
			if (texture.getAnimationChannel() == animDeath) {
				this.health--;
				entity.removeFromWorld();
			}
		});
	}
	protected void commonEnemyFunc(double tpf, boolean isProtecting) {
		if (waitTimer.elapsed(Duration.seconds(1.2))) {
			enemyWaiting = false;
		}
		if (this.isDead && texture.getAnimationChannel() != animDeath && !isProtecting && !isAttacking
				&& !this.hasTakenDamage) {
			texture.playAnimationChannel(animDeath);
		}
		if (isGettingHit && texture.getAnimationChannel() != animHit && !isDead && !isProtecting && !isAttacking
				&& !this.hasTakenDamage) {
			texture.playAnimationChannel(animHit);
		}

		if (this.hasTakenDamage && texture.getAnimationChannel() != animHit && !isGettingHit && !isDead && !isProtecting
				&& !isAttacking) {
			texture.playAnimationChannel(animHit);
		}

		if (speed != 0 && texture.getAnimationChannel() != animWalk && !isGettingHit && !isDead && !isProtecting
				&& !isAttacking && !this.hasTakenDamage)
			texture.loopAnimationChannel(animWalk);
		if (goingRight) {
			entity.setScaleX(-1.2);
		}
		if (!goingRight) {
			entity.setScaleX(1.2);
		}
		entity.translateX(goingRight ? speed * tpf : -speed * tpf);
	}
}
