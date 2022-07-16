package pl.tuso.entities.type;

import org.jetbrains.annotations.NotNull;
import pl.tuso.entities.animation.Animation;
import pl.tuso.entities.animation.AnimationPosition;
import pl.tuso.entities.animation.Frame;

public interface Animator {
    void playAnimation(String name);

    Animation getAnimation();

    String getAnimationName();

    AnimationPosition getAnimationPosition();

    boolean isAnimationEnded();

    void loadFrame(@NotNull Frame frame);
}
