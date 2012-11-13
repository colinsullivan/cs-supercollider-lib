RunningWaterEnvironment : PatchEnvironment {

  var <>hellSlider;

  load_samples {
    arg callback;

    var sfxRoot = "/Volumes/Secondary/Samples/Recorded Sounds/Sound Effects/",
      me = this;


    Buffer.read(Server.default, sfxRoot ++ "running water stream.aif", action: {
      arg buf;

      me.buf = buf;

      callback.value();
    });
  }

  load_patch {
    super.load_patch();

    this.patch = Patch(Instr.at("sfx.RunningWaterStreamAutomated"), (
      buffer: this.buf,
      gate: KrNumberEditor.new(0, \gate.asSpec())
    ));
    this.patch.prepareForPlay();
  }

  init_gui {
    arg params;

    var toggleButton,
      patch = this.patch,
      layout = params['layout'],
      interface = this.interface;

    super.init_gui(params);

    patch.amp.gui(layout);
    patch.hellMin.gui(layout);
    patch.hellMax.gui(layout);
    patch.hellFreq.gui(layout);

    toggleButton = Button(layout, Rect(10, 10, 100, 30))
      .states_([
        ["on"],
        ["off"]
      ])
      .action_({
        arg toggleButton;

        patch.set(\gate, toggleButton.value);
      });

    /* slider mapped to both cutoff freq and bit crusher */
    /*this.hellSlider = Slider(layout, Rect(10, 10, 100, 20))
      .action_({
        arg hellSlider;
        var inverseHell = 1.0 - hellSlider.value;
        patch.lowPassFreq.value = patch.lowPassFreq.spec.map(inverseHell);
        patch.decimatorRate.value = patch.decimatorRate.spec.map(inverseHell);
      });*/
  }

  load_external_controller_mappings {
    var hellSlider = this.hellSlider;
    
    super.load_external_controller_mappings();

    this.uc33Controller.mapCCS(1, 'knu2', {
      arg ccval;

      {hellSlider.valueAction = ccval / 127;}.defer();
    });

    this.map_uc33_to_patch('sl2', \amp);
  }

}
