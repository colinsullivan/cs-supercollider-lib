SecondSynkopater : PerformanceEnvironmentComponent {
  var <>schedulerTask,
    <>hardSineVoicer,
    <>numNotes,
    <>phaseEnv,
    <>phaseEnvView,
    <>phaseEnvModulator,
    <>phaseQuantizationBeats;

  init {
    arg params;

    this.numNotes = KrNumberEditor.new(1, ControlSpec(1, 8, step: 1));
    this.phaseEnv = Env.new([-1.0, 1.0], [1.0], [0, 0]);
    this.phaseEnvModulator = KrNumberEditor.new(1.0, \bipolar);

    this.phaseQuantizationBeats = 1.0;
    
    super.init(params);
  }

  init_tracks {
    super.init_tracks();
  }

  load_samples {
    arg callback;

    
  }

  init_patches {
    super.init_patches();

    this.hardSineVoicer = Voicer.new(
      2,
      Instr("cs.synths.HardSine")
    );

  }

  play_patches_on_tracks {
    super.play_patches_on_tracks();
    
    this.hardSineVoicer.target_(this.outputChannel);
  }

  /**
   *  Update the grid on the envelope display
   **/
  //sync_grid_with_numNotes {
    //this.phaseEnvView.grid.x = this.numNotes.value;
  //}

  /**
   *  Update note phase envelope to change rhythm of notes, as well as
   *  the GUI to reflect.
   **/
  modulate_note_phase {
    arg val;

    var envelopeDisplayVal = (val * 0.5) + 0.5;

    this.phaseEnv.levels = [val, -1.0 * val];
    this.phaseEnvView.value_([[0, 1], [envelopeDisplayVal, 1.0 - envelopeDisplayVal]]);
  }

  load_environment {
    var me = this,
      envelopeDisplayVal;
    super.load_environment();

    /**
     *  When `numNotes` is changed, modify drawing.
     **/
    //this.numNotes.action = {
      //{
        //me.sync_grid_with_numNotes();
      //}.defer();
    //};

    /**
     *  When synkopation curve is changed, modify envelope.
     **/
    this.phaseEnvModulator.action = {
      arg val;
      {
        me.modulate_note_phase(val);
      }.defer(); // TODO: Might need delay here for performance
    };

    this.schedulerTask = {
      var nextTriggerTime,
        t = TempoClock.default,
        numNotes = me.numNotes.value,
        notePhase,
        notePhaseModulation,
        noteBeat,
        noteLatency,
        quantizedPhaseEnv = me.phaseEnv.asSignal(numNotes);

      "--------------".postln();
      "scheduler task".postln();

      // schedule notes
      for (0, numNotes - 1, {
        arg i;
        
        "i:".postln;
        i.postln;

        // amount note is shifted due to envelope curve
        notePhaseModulation = (
          quantizedPhaseEnv[i] * me.phaseQuantizationBeats
        );

        "notePhaseModulation:".postln;
        notePhaseModulation.postln;

        // phase of note (which beat it sits on)
        notePhase = (i / numNotes) * t.beatsPerBar + notePhaseModulation;

        // offset from the next bar
        noteBeat = t.beatsPerBar + t.nextTimeOnGrid(
          t.beatsPerBar,
          notePhase
        );
        noteLatency = t.beats2secs(noteBeat) - t.seconds;
        
        me.hardSineVoicer.trigger1(
          440 * 32,
          lat: noteLatency
        );

      });

      // return value, schedule notes again next bar
      if (me.playing, {
        TempoClock.default.beatsPerBar;
      }, {
        nil;
      });
    }
  }

  on_play {
    var clock = TempoClock.default(),
      nextBar = clock.nextTimeOnGrid(clock.beatsPerBar);

    super.on_play();

    // start scheduler task next bar
    "calling schedAbs to start scheduler task".postln();
    clock.schedAbs(nextBar, this.schedulerTask);    
  }

  init_gui {
    arg params;
    
    var layout = params['layout'],
      labelWidth = 80,
      me = this;

    super.init_gui(params);

    layout.flow({
      arg layout;

      ArgNameLabel("numNotes", layout, labelWidth);
      me.numNotes.gui(layout);
      layout.startRow();
      
      //ArgNameLabel("phaseEnv", layout, labelWidth);
      //me.phaseEnvEditor.gui(layout);
      //layout.startRow();
      me.phaseEnvView = EnvelopeView(
        layout,
        Rect(0, 0, labelWidth, labelWidth)
      );
      me.phaseEnvView.editable = false;
      //me.phaseEnvView.gridOn = true;
      //me.phaseEnvView.grid = Point.new(4);
      layout.startRow();

      ArgNameLabel("phaseEnvModulator", layout, labelWidth);
      me.phaseEnvModulator.gui(layout);
      layout.startRow();

    });

  }

  init_uc33_mappings {
    super.init_uc33_mappings();

    this.map_uc33_to_property(\knu5, \phaseEnvModulator);
  }

}
