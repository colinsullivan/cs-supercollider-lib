GranularChaosEnvironment : PatchEnvironmentComponent {

  var <>grainEnvBuf;

  load_samples {
    arg callback;
    var sfxRoot = "/Volumes/Secondary/Samples/Recorded Sounds/Sound Effects/",
      winenv = Env.sine(4.0),
      me = this;

    /*"GranularChaosEnvironment.load_samples".postln;*/

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

    /*"GranularChaosEnvironment.load_patch".postln;*/

    this.patch = Patch(Instr.at("cs.sfx.GranularChaos"), (
      buffer: this.buf,
      envbuf: this.grainEnvBuf,
      gate: KrNumberEditor.new(1, \gate)
      /*done_callback: {
        "done!".postln;
      },*/
    ));
  }

  init_external_controller_mappings {
    super.init_external_controller_mappings();

    /*"GranularChaosEnvironment.init_external_controller_mappings".postln;*/
    
    /**
     *  UC-33 Mappings
     **/
    if (this.uc33Controller != nil, {
      /* slider 1 to amplitude */
      /*this.map_uc33_to_patch('sl1', \amp);*/

      /* knob 25 to position */
      this.map_uc33_to_patch('knu5', \pointer);

      /* knob 17 to pitch */
      this.map_uc33_to_patch('knm5', \pitch);
    
    });
  }

  init_gui {
    arg params;
    var granularButton,
      label,
      patch = this.patch,
      layout = params['layout'],
      labelWidth = 75;
  
    super.init_gui(params);

    /*"GranularChaosEnvironment.init_gui".postln;*/

    layout.flow({
      arg layout;
      ArgNameLabel("pos", layout, labelWidth);
      patch.pointer.gui(layout);
      layout.startRow();
      
      ArgNameLabel("pitch", layout, labelWidth);
      patch.pitch.gui(layout);
      layout.startRow();
      
      ArgNameLabel("amp", layout, labelWidth);
      patch.amp.gui(layout);

    });

    /*window = Window.new("Granular", Rect(*/
      /*(screenBounds.width() / 2.0) - (windowWidth / 2.0),*/
      /*(screenBounds.height() / 2.0) - (windowHeight / 2.0),*/
      /*windowWidth,*/
      /*windowHeight*/
    /*));*/

    /*layout.flow({
      arg layout;
      granularButton = Button(layout, Rect(10, 10, 100, 30)).states_([
        ["on"],
        ["off"]
      ])
      .action_({ arg granularButton;

        if (granularButton.value == 1, {
          patch.set(\gate, 1);
        }, {
          patch.set(\gate, 0);
        });

      });

    });*/
  }
}
