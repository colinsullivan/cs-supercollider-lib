PatchEnvironmentComponent : PerformanceEnvironmentComponent {

  var <>buf,
    <>patch;


  init {
    arg params;
    
    super.init(params);

    /*"PatchEnvironmentComponent.init".postln;*/

    
  }

  on_play {
    //this.patch.play(bus: Bus.audio(Server.default, this.outputBus));
    this.outputChannel.play(this.patch);
  }

  load_environment {
    this.load_patch();
  }

  load_patch {
    /*"PatchEnvironmentComponent.load_samples".postln;*/
    // subclasses should instantiate Patch objects here and call prepareForPlay
  }

  /**
   *  Map a property of the patch to a UC-33 controller knob or slider.
   *
   *  @param  String  A string containing a key used to identify the
   *  knob or slider on the controller.  Ex: 'sl1'.
   *  @param  Symbol  Used as a key to identify the property of the patch
   *  to control with the aforementioned controller knob.  Ex: \amp.
   **/
  map_uc33_to_patch {
    arg controllerComponent, patchPropertyKey, patch = this.patch;
    var patchProperty;

    /*"PatchEnvironmentComponent.map_uc33_to_patch".postln;*/
    
    patchProperty = patch.performMsg([patchPropertyKey]);

    this.uc33Controller.mapCCS(1, controllerComponent, {
      arg ccval;

      patchProperty.value = patchProperty.spec.map(ccval / 127);
    });
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
