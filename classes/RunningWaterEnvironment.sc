RunningWaterEnvironment : PatchEnvironment {

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

  }

  load_external_controller_mappings {
    
    super.load_external_controller_mappings();

    this.map_uc33_to_patch('sl2', \amp);
  }

}
