GranularChaosEnvironment : PatchPerformanceEnvironment {

  var <>grainEnvBuf;

  load_samples {
    arg callback;
    var sfxRoot = "/Volumes/Secondary/Samples/Recorded Sounds/Sound Effects/",
      winenv = Env.sine(4.0),
      me = this;

    "GranularChaosEnvironment.load_samples".postln;

    Buffer.read(Server.default, sfxRoot ++ "bong-1.aif", action: {
      arg buf;

      me.buf = buf;

      Buffer.sendCollection(Server.default, winenv.discretize, 1, action: {
        arg envBuf;

        me.grainEnvBuf = envBuf;

        callback.value();
      });
    });
  }

  load_patch {
    super.load_patch();

    "GranularChaosEnvironment.load_patch".postln;

    this.patch = Patch(Instr.at("sfx.GranularChaos"), (
      buffer: this.buf,
      envbuf: this.grainEnvBuf,
      gate: KrNumberEditor.new(0, \gate.asSpec())
      /*done_callback: {
        "done!".postln;
      },*/
    ));
    this.patch.prepareForPlay();

  }

  load_external_controller_mappings {
    super.load_external_controller_mappings();

    "GranularChaosEnvironment.load_external_controller_mappings".postln;
    
    /**
     *  UC-33 Mappings
     **/
    /* slider 1 to amplitude */
    this.map_uc33_to_patch('sl1', \amp);

    /* knob 25 to position */
    this.map_uc33_to_patch('knu1', \pointer);

    /* knob 17 to pitch */
    this.map_uc33_to_patch('knm1', \pitch);
  }

  load_gui {
    arg layout, metaPatch;
    var screenBounds = Window.screenBounds(),
      windowWidth = 480,
      windowHeight = 256,
      granularButton,
      window,
      patch = this.patch,
      me = this;
   
    "GranularChaosEnvironment.load_gui".postln;

    patch.pointer.gui(layout);
    patch.pitch.gui(layout);
    patch.amp.gui(layout);

    /*window = Window.new("Granular", Rect(*/
      /*(screenBounds.width() / 2.0) - (windowWidth / 2.0),*/
      /*(screenBounds.height() / 2.0) - (windowHeight / 2.0),*/
      /*windowWidth,*/
      /*windowHeight*/
    /*));*/
    granularButton = Button(layout, Rect(10, 10, 100, 30)).states_([
      ["On"],
      ["Off"]
    ])
    .action_({ arg granularButton;

      if (granularButton.value == 1, {
        patch.set(\gate, 1);
      }, {
        patch.set(\gate, 0);
      });

    });
  }

}
