Synkopater : PerformanceEnvironmentComponent {
  var <>inputTrackOne,
    <>inputTrackTwo,
    <>delayTrackOne,
    <>delayTrackTwo,
    <>impulsePatchOne,
    <>impulsePatchTwo,
    <>delayPatchOne,
    <>delayPatchTwo,
    <>synkopationControlOne,
    <>synkopationControlTwo,
    <>triggerDelayOne,
    <>triggerDelayTwo,
    <>playTaskOne,
    <>playTaskTwo,
    <>ampAndToggleSlider;

  init {
    arg params;
    
    this.triggerDelayOne = KrNumberEditor.new(0.0);
    this.triggerDelayTwo = KrNumberEditor.new(0.0);
    
    //  these are the controls that will map to the parameters of the two
    //  delays and input sources
    this.synkopationControlOne = KrNumberEditor.new(0.0, \unipolar);
    this.synkopationControlTwo = KrNumberEditor.new(0.0, \unipolar);

    this.ampAndToggleSlider = KrNumberEditor.new(0.0, \amp);

    super.init(params);

  }

  init_tracks {
    super.init_tracks();

    // these tracks will run the delay effect
    this.delayTrackOne = MixerChannel.new(
      "delayOne",
      Server.default,
      2, 2,
      outbus: this.outputChannel
    );

    this.delayTrackTwo = MixerChannel.new(
      "delayTwo",
      Server.default,
      2, 2,
      outbus: this.outputChannel
    );
  
    // these are the patches for the delay effect 
    this.delayPatchOne = FxPatch("cs.Synkopater.SynkopaterDelay", (
      numChan: 2,
      delaySecs: KrNumberEditor.new(0.0, ControlSpec(0.0, 8.0)),
      feedbackCoefficient: 0.8
    ));

    this.delayPatchTwo = FxPatch("cs.Synkopater.SynkopaterDelay", (
      numChan: 2,
      delaySecs: KrNumberEditor.new(0.0, ControlSpec(0.0, 8.0)),
      feedbackCoefficient: 0.8
    ));
    
    //  these tracks will route the input to the delay and directly to the
    //  output. 
    this.inputTrackOne = MixerChannel.new(
      "inputTrackOne",
      Server.default,
      2, 2,
      outbus: this.outputChannel
    );
    this.inputTrackOne.newPostSend(this.delayTrackOne, 1.0);

    this.inputTrackTwo = MixerChannel.new(
      "inputTrackTwo",
      Server.default,
      2, 2,
      outbus: this.outputChannel
    );
    this.inputTrackTwo.newPostSend(this.delayTrackTwo, 1.0);
  }

  init_patches {
    super.init_patches();
    
    //  these are the impulse input patches.
    this.impulsePatchOne = Voicer.new(
      2,
      Instr("cs.percussion.Impulsive"),
      target: this.inputTrackOne
    );

    this.impulsePatchTwo = Voicer.new(
      2,
      Instr("cs.percussion.Impulsive"),
      target: this.inputTrackTwo
    );

  }

  play_patches_on_tracks {
    this.delayTrackOne.playfx(this.delayPatchOne);
    this.delayTrackTwo.playfx(this.delayPatchTwo);
  }

  handle_synkopation_control_changed {
    arg whichControl,
      val;

    var currentBeatsPerSecond = Tempo.tempo,
      newDelayTime,
      newTriggerRate;

    newDelayTime = currentBeatsPerSecond + (currentBeatsPerSecond * val);
    newTriggerRate = currentBeatsPerSecond * val;

    this.performMsg([("delayPatch"++whichControl).asSymbol()]).delaySecs.value = newDelayTime;
    this.performMsg([("triggerDelay"++whichControl).asSymbol()]).value = newTriggerRate;

  }

  load_environment {
    var me = this;
    super.load_environment();
    this.synkopationControlOne.action = {
      arg val;
      me.handle_synkopation_control_changed("One", val);
    };

    this.synkopationControlTwo.action = {
      arg val;
      me.handle_synkopation_control_changed("Two", val);
    };

    this.playTaskOne = {
      me.impulsePatchOne.trigger1(440, lat: me.triggerDelayOne.value);

      if (me.playing, {
        TempoClock.default.sched(2, me.playTaskOne);
      });
    };

    this.playTaskTwo = {
      me.impulsePatchTwo.trigger1(880, lat: me.triggerDelayTwo.value);
      if (me.playing, {
        TempoClock.default.sched(2, me.playTaskTwo);    
      });
    };

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

  }

  on_play {
    var me = this,
      nextBar,
      oneBeatIntoNextBar,
      tempoClock = TempoClock.default;

    super.on_play();

    nextBar = tempoClock.nextTimeOnGrid(tempoClock.beatsPerBar);

    tempoClock.schedAbs(nextBar, this.playTaskOne);
    tempoClock.schedAbs(nextBar + 1, this.playTaskTwo);
  }

  init_gui {
    arg params;
    var labelWidth = 75,
      layout = params['layout'];

    super.init_gui(params);

    layout.flow({
      arg layout;

      ArgNameLabel("one", layout, labelWidth);
      this.synkopationControlOne.gui(layout);
      layout.startRow();

      ArgNameLabel("two", layout, labelWidth);
      this.synkopationControlTwo.gui(layout);
      layout.startRow();

      ArgNameLabel("amp", layout, labelWidth);
      this.ampAndToggleSlider.gui(layout);
      layout.startRow();

    });
  }
  
  init_uc33_mappings {
    this.map_uc33_to_property(\knu6, \synkopationControlOne);
    this.map_uc33_to_property(\knm6, \synkopationControlTwo);
    this.map_uc33_to_property(\sl6, \ampAndToggleSlider);
  }
}