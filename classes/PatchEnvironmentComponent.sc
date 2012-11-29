PatchEnvironmentComponent : PerformanceEnvironmentComponent {

  var <>buf,
    <>patch,
    <>interface,
    <>outputBus;


  init {
    arg params;
    var me = this;

    super.init(params);
    
    this.outputBus = 0;

    /*"PatchEnvironmentComponent.init".postln;*/

    this.load_samples({

      me.interface = Interface({
        me.load_patch();
      }).onPlay_({
        me.on_play();
      }).onStop_({
        me.on_stop();
      });

      me.interface.gui = {
        arg layout, metaPatch;
        me.init_gui((
          window: layout.parent.parent,
          layout: layout,
          metaPatch: metaPatch
        ));
        me.init_external_controller_mappings();
      };

      {
        me.interface.gui();
        me.init_done_callback.value();
      }.defer(1);
    
    });
  }

  on_play {
    this.patch.play(bus: Bus.audio(Server.default, this.outputBus));
  }

  on_stop {
  
  }

  load_samples {
    arg callback;
    // subclasses should load samples before using callback
    callback.value();
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
