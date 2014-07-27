Synkopants : PerformanceEnvironmentComponent {
  var <>bufManager,
    <>ampAndToggleSlider,
    <>elements;

  init {
    arg params;

    this.elements = [

      SynkopantsElement.new((
        parent: this,
        bufKey: \kick
      )),

      SynkopantsElement.new((
        parent: this,
        bufKey: \snare01
      )),
    
    ];

    this.ampAndToggleSlider = KrNumberEditor.new(0.0, \amp);
    
    //  create the buffer manager that will load the samples we need for this
    //  patch.
    this.bufManager = BufferManager.new().init((
      rootDir: "/Users/colin/Samples/Recorded Sounds/Sound Effects/"
    ));

    super.init(params);
  }

  load_samples {
    arg callback;

    this.bufManager.load_bufs([
      ["susans_coffee_hits/crunchy.wav", \crunchy],
      ["susans_coffee_hits/low.wav", \low],
      ["susans_coffee_hits/pitched-01.wav", \pitched01],
      ["susans_coffee_hits/pitched-02.wav", \pitched02],
      ["susans_coffee_hits/pitched-03.wav", \pitched03],
      ["susans_coffee_hits/snare-01.wav", \snare01],
      ["susans_coffee_hits/snare-02.wav", \snare02],
      ["susans_coffee_hits/kick.wav", \kick]
    ], callback);

  }

  /**
   *  For each element, make sure patch is created.
   */
  init_patches {
    super.init_patches();


    this.elements.do({
      arg element;

      element.init_patch();
    });

  }

  /**
   *  For each element, make sure patch output is routed to our channel.
   */
  play_patches_on_tracks {
    super.play_patches_on_tracks();

    this.elements.do({
      arg element;

      this.outputChannel.addPatch(element.patch);
    });
  }

  load_environment {
    var me = this;

    super.load_environment();
    
    // when amplitude and toggle slider is changed
    this.ampAndToggleSlider.action = {
      arg val;

      // set volume of master output (affecting all elements)
      me.outputChannel.level = val;

      // if slider is zero, and patch is playing, stop patch
      if (me.playing && val == 0, {
        me.interface.stop();
      }, {
        // if slider is non-zero and patch is stopped, start patch
        if (me.playing == false && val != 0, {
          me.interface.play();
        });
      });
    };

    this.elements.do({
      arg element;

      element.load_environment();
    });

  }


  on_play {
    
    this.elements.do({
      arg element;

      element.on_play();
    });
    
    super.on_play();

  }

  init_gui {
    arg params;
    
    var layout = params['layout'],
      labelWidth = 80,
      me = this;

    super.init_gui(params);

    layout.flow({
      arg layout;

      ArgNameLabel("amp", layout, labelWidth);
      this.ampAndToggleSlider.gui(layout);
      layout.startRow();
    });

    this.elements.do({
      arg element;

      layout.flow({
        arg layout;

        CXLabel(layout, element.bufKey.asString(), 400);
        layout.startRow();

        ArgNameLabel("numNotes", layout, labelWidth);
        element.numNotes.gui(layout);
        layout.startRow();
        
        element.phaseEnvView = EnvelopeView(
          layout,
          Rect(0, 0, labelWidth, labelWidth)
        );
        element.phaseEnvView.editable = false;
        element.phaseEnvView.setEnv(element.phaseEnv);
        layout.startRow();

        ArgNameLabel("phaseEnvModulator", layout, labelWidth);
        element.phaseEnvModulator.gui(layout);
        layout.startRow();
        
        ArgNameLabel("phaseOffset", layout, labelWidth);
        element.phaseOffset.gui(layout);
        layout.startRow();
        
        ArgNameLabel("amp", layout, labelWidth);
        element.ampSlider.gui(layout);
        layout.startRow();
      });

    });


  }

  init_uc33_mappings {
    super.init_uc33_mappings();

    //this.map_uc33_to_property(\knu5, \numNotes);
    //this.map_uc33_to_property(\knm5, \phaseEnvModulator);
    //this.map_uc33_to_property(\sl5, \ampAndToggleSlider);
  }

}
