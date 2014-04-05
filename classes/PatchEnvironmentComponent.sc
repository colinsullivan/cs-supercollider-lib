PatchEnvironmentComponent : PerformanceEnvironmentComponent {

  var <>buf,
    <>patch,
    <>ampAndToggleSlider;


  init {
    arg params;
    
    super.init(params);

    this.ampAndToggleSlider = KrNumberEditor.new(0.0, \amp);
    
    /*"PatchEnvironmentComponent.init".postln;*/

    
  }

  on_play {
    //this.outputChannel.play(this.patch);
    this.patch.play();
  }

  on_stop {
    this.patch.stop();
  }

  play_patches_on_tracks {
    this.outputChannel.addPatch(this.patch);
  }

  load_environment {
    super.load_environment();

    // when amplitude and toggle slider is changed
    this.ampAndToggleSlider.action = {
      arg val;

      // set volume of output
      this.outputChannel.level = val;

      // if slider is zero, and patch is playing, stop patch
      if (this.playing && val == 0, {
        this.interface.stop();
      }, {
        // if slider is non-zero and patch is stopped, start patch
        if (this.playing == false && val != 0, {
          this.interface.play();
        });
      });
    };
  }


  /**
   *  Map a property of the patch to a UC-33 controller knob or slider.
   *
   *  @param  String  A string containing a key used to identify the
   *  knob or slider on the controller.  Ex: 'sl1'.
   *  @param  Symbol|Array  Used as a key or keys to identify the property of
   *  the patch to control with the aforementioned controller knob.  Ex: \amp.
   **/
  map_uc33_to_patch {
    // mapTo has a different default than in parent class
    arg controllerComponent, propertyKeys, mapTo = this.patch;

    this.map_uc33_to_property(controllerComponent, propertyKeys, mapTo);

  }

  map_softStep_to_patch {
    arg controllerComponent, patchPropertyKey, patch = this.patch;
    var patchProperty;

    if (patchPropertyKey.isSymbol(), {
      patchProperty = patch.performMsg([patchPropertyKey]);

      this.softStepController.mapCC(controllerComponent, {
        arg ccval;

        patchProperty.value = patchProperty.spec.map(ccval / 127);
      });
    });

    if (patchPropertyKey.isArray(), {
      this.softStepController.mapCC(controllerComponent, {
        arg ccval;

        for (0, patchPropertyKey.size - 1, {
          arg i;

          patchProperty = patch.performMsg([patchPropertyKey[i]]);
          patchProperty.value = patchProperty.spec.map(ccval / 127);
        });
      }); 
    });
  }


}
