PatchEnvironmentComponent : PerformanceEnvironmentComponent {

  var <>buf,
    <>patch,
    <>uc33Controller,
    <>softStepController,
    <>outputBus;


  init {
    arg params;
    this.outputBus = 0;

    super.init(params);

    /*"PatchEnvironmentComponent.init".postln;*/

    
  }

  on_play {
    this.patch.play(bus: Bus.audio(Server.default, this.outputBus));
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

  init_external_controller_mappings {
    
    /*this.uc33Controller = UC33Ktl.new();*/
    var uc33Port,
      softStepPort;
    
    super.init_external_controller_mappings();
    
    uc33Port = MIDIIn.findPort("UC-33 USB MIDI Controller", "Port 1");
    /*uc33Port = MIDIIn.findPort("(out) SuperCollider", "(out) SuperCollider");*/
    softStepPort = MIDIIn.findPort("SoftStep Share", "SoftStep Share");

    if (uc33Port != nil, {
      // sub-classes should use this UC33Ktl instance to assign knobs and such.
      this.uc33Controller = UC33Ktl.new(
        uc33Port.uid
      );
    }, {
      // sub-classes should check to see if uc33Controller is nil to determine
      // if it is currently connected.
      this.uc33Controller = nil;
    });

    if (softStepPort != nil, {
      this.softStepController = SoftStepKtl.new(softStepPort.uid);    
    }, {
      this.softStepController = nil;
    });

    /*"PatchEnvironmentComponent.init_external_controller_mappings".postln;*/
  }

}
