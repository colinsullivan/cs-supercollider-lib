SynkopantsElement : Object {
  /**
   *  The task that is running, scheduling future the playback of this element.
   **/
  var <>schedulerTask,
    <>numNotes,
    <>phaseEnv,
    <>phaseEnvView,
    <>phaseEnvModulator,
    <>phaseQuantizationBeats,
    <>phaseQuantizationSpec,
    <>ampSlider,
    <>patch,
    <>phaseOffset,
    <>parent,
    <>bufKey;

  *new {
    arg params;

    ^super.new.init(params);
  }
  init {
    arg params;

    this.parent = params['parent'];

    this.bufKey = params['bufKey'];
    
    this.numNotes = KrNumberEditor.new(1, ControlSpec(1, 32, step: 1));
    //this.phaseEnv = Env.new([-1.0, 1.0], [1.0], [0, 0]);
    this.phaseEnv = Env.new([0.5, 0.5], [1.0], [0, 0]);
    this.phaseEnvModulator = KrNumberEditor.new(1.0, \unipolar);

    this.phaseOffset = KrNumberEditor.new(0, ControlSpec(0, 4, step: 1.0 / 16.0));

    this.phaseQuantizationBeats = 1.0;
    this.phaseQuantizationSpec = ControlSpec.new(-1.0, 1.0, \lin, 1.0/16.0);
    
    this.ampSlider = KrNumberEditor.new(0.0, \amp);
  }

  init_patch {
    this.patch = Patch("cs.sfx.PlayBuf", (
      buf: this.parent.bufManager.bufs[this.bufKey],
      gate: 1,
      attackTime: 0.01,
      releaseTime: 0.01,
      amp: this.ampSlider
    ));
  }

  load_environment {
    var me = this;
  
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
      outputChannel = this.parent.outputChannel,
      patch = this.patch,
      phaseQuantizationBeats = this.phaseQuantizationBeats,
      phaseQuantizationSpec = this.phaseQuantizationSpec,
      phaseOffset = this.phaseOffset.value;

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
      notePhase = (i / numNotes) * t.beatsPerBar + notePhaseModulation + phaseOffset;

      // offset from the next bar
      noteBeat = t.beatsPerBar + t.nextTimeOnGrid(
        t.beatsPerBar,
        notePhase
      );
      noteLatency = t.beats2secs(noteBeat) - t.seconds;

      //percPatches[i % percPatches.size()].playToMixer(
      patch.playToMixer(
        outputChannel,
        atTime: noteLatency
      );

      /*this.hardSineVoicer.trigger1(
        440 * 32,
        lat: noteLatency
      );*/

    });

    // return value, schedule notes again next bar
    if (this.parent.playing, {
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
    if (this.parent.playing == false, {
      // start scheduler task next bar
      "calling schedAbs to start scheduler task".postln();
      clock.schedAbs(nextBar, {
        me.schedule_notes();
      });
    });
  }

}
