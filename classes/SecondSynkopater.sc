SecondSynkopater : PerformanceEnvironmentComponent {
  var <>schedulerTask,
    <>hardSineVoicer,
    <>numNotes,
    <>phaseEnv,
    <>phaseEnvView,
    <>phaseEnvModulator,
    <>phaseQuantizationBeats,
    <>phaseQuantizationSpec,
    <>bufManager,
    <>percPatches,
    <>ampAndToggleSlider;

  init {
    arg params;

    this.ampAndToggleSlider = KrNumberEditor.new(0.0, \amp);
    
    this.numNotes = KrNumberEditor.new(1, ControlSpec(1, 8, step: 1));
    //this.phaseEnv = Env.new([-1.0, 1.0], [1.0], [0, 0]);
    this.phaseEnv = Env.new([0.5, 0.5], [1.0], [0, 0]);
    this.phaseEnvModulator = KrNumberEditor.new(1.0, \unipolar);

    this.phaseQuantizationBeats = 1.0;
    this.phaseQuantizationSpec = ControlSpec.new(-1.0, 1.0, \lin, 1.0/16.0);

    //  create the buffer manager that will load the samples we need for this
    //  patch.
    this.bufManager = BufferManager.new().init((
      rootDir: "/Volumes/Secondary/Samples/Recorded Sounds/Sound Effects/"
    ));

    super.init(params);
  }

  init_tracks {
    super.init_tracks();
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

  init_patches {
    super.init_patches();

    this.hardSineVoicer = Voicer.new(
      2,
      Instr("cs.synths.HardSine")
    );

    this.percPatches = [
      Patch("cs.sfx.PlayBuf", (
        buf: this.bufManager.bufs[\kick],
        gate: 1,
        attackTime: 0.01,
        releaseTime: 0.01
      )),
      Patch("cs.sfx.PlayBuf", (
        buf: this.bufManager.bufs[\snare01],
        gate: 1,
        attackTime: 0.01,
        releaseTime: 0.01
      )),
      Patch("cs.sfx.PlayBuf", (
        buf: this.bufManager.bufs[\crunchy],
        gate: 1,
        attackTime: 0.01,
        releaseTime: 0.01
      )),
      Patch("cs.sfx.PlayBuf", (
        buf: this.bufManager.bufs[\snare02],
        gate: 1,
        attackTime: 0.01,
        releaseTime: 0.01
      )),
      Patch("cs.sfx.PlayBuf", (
        buf: this.bufManager.bufs[\pitched01],
        gate: 1,
        attackTime: 0.01,
        releaseTime: 0.01
      )),
      Patch("cs.sfx.PlayBuf", (
        buf: this.bufManager.bufs[\pitched02],
        gate: 1,
        attackTime: 0.01,
        releaseTime: 0.01
      )),
      Patch("cs.sfx.PlayBuf", (
        buf: this.bufManager.bufs[\pitched03],
        gate: 1,
        attackTime: 0.01,
        releaseTime: 0.01
      ))
    ];

  }

  play_patches_on_tracks {
    super.play_patches_on_tracks();
    
    this.hardSineVoicer.target_(this.outputChannel);

    this.percPatches.do({
      arg percPatch;

      this.outputChannel.addPatch(percPatch);
    });
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

    this.phaseEnv.levels = [val, 1.0 - val];
    //this.phaseEnv.curves = [-4.0 * val];
    //this.phaseEnvView.value_([[0, 1], [envelopeDisplayVal, 1.0 - envelopeDisplayVal]]);
    this.phaseEnvView.setEnv(this.phaseEnv);
    //this.phaseEnvView.curves = [-4.0 * val];
  }

  load_environment {
    var me = this;

    super.load_environment();

    /**
     *  When `numNotes` is changed, modify drawing.
     **/
    //this.numNotes.action = {
      //{
        //me.sync_grid_with_numNotes();
      //}.defer();
    //};
    
    // when amplitude and toggle slider is changed
    this.ampAndToggleSlider.action = {
      arg val;

      // set volume of output
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

    /**
     *  When synkopation curve is changed, modify envelope.
     **/
    this.phaseEnvModulator.action = {
      arg val;
      {
        me.modulate_note_phase(val);
      }.defer(); // TODO: Might need delay here for performance
    };
  }

  /**
   *  On each bar, run this method to prepare all notes for the next bar.
   **/
  schedule_notes {
    var nextTriggerTime,
      t = TempoClock.default,
      numNotes = this.numNotes.value,
      notePhase,
      notePhaseModulation,
      noteBeat,
      noteLatency,
      quantizedPhaseEnv = this.phaseEnv.asSignal(numNotes),
      outputChannel = this.outputChannel,
      percPatches = this.percPatches,
      phaseQuantizationBeats = this.phaseQuantizationBeats,
      phaseQuantizationSpec = this.phaseQuantizationSpec;

    //"--------------".postln();
    //"scheduler task".postln();

    // schedule notes
    for (0, numNotes - 1, {
      arg i;
      
      // amount note is shifted due to envelope curve
      notePhaseModulation = (
        this.phaseQuantizationSpec.map(quantizedPhaseEnv[i]) * phaseQuantizationBeats
      );

      // phase of note (which beat it sits on)
      notePhase = (i / numNotes) * t.beatsPerBar + notePhaseModulation;

      // offset from the next bar
      noteBeat = t.beatsPerBar + t.nextTimeOnGrid(
        t.beatsPerBar,
        notePhase
      );
      noteLatency = t.beats2secs(noteBeat) - t.seconds;

      percPatches[i % percPatches.size()].playToMixer(
        outputChannel,
        atTime: noteLatency
      );

      /*this.hardSineVoicer.trigger1(
        440 * 32,
        lat: noteLatency
      );*/

    });

    // return value, schedule notes again next bar
    if (this.playing, {
      ^TempoClock.default.beatsPerBar;
    }, {
      ^nil;
    });
  
  }

  on_play {
    var clock = TempoClock.default(),
      nextBar = clock.nextTimeOnGrid(clock.beatsPerBar),
      me = this;

    // had to do this if block because on_play is called
    // multiple times when mapped to the amp slider.
    if (this.playing == false, {
      // start scheduler task next bar
      "calling schedAbs to start scheduler task".postln();
      clock.schedAbs(nextBar, {
        me.schedule_notes();
      });
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

      ArgNameLabel("numNotes", layout, labelWidth);
      me.numNotes.gui(layout);
      layout.startRow();
      
      me.phaseEnvView = EnvelopeView(
        layout,
        Rect(0, 0, labelWidth, labelWidth)
      );
      me.phaseEnvView.editable = false;
      me.phaseEnvView.setEnv(this.phaseEnv);
      layout.startRow();

      ArgNameLabel("phaseEnvModulator", layout, labelWidth);
      me.phaseEnvModulator.gui(layout);
      layout.startRow();
      
      ArgNameLabel("amp", layout, labelWidth);
      this.ampAndToggleSlider.gui(layout);
      layout.startRow();

    });

  }

  init_uc33_mappings {
    super.init_uc33_mappings();

    this.map_uc33_to_property(\knu5, \numNotes);
    this.map_uc33_to_property(\knm5, \phaseEnvModulator);
    this.map_uc33_to_property(\sl5, \ampAndToggleSlider);
  }

}
