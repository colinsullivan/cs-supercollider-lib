SynkopaterDelay : PerformanceEnvironmentComponent {
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
    <>delayFactorControl,
    <>delayFeedbackControl,
    <>triggerDelayOne,
    <>triggerDelayTwo,
    <>ampAndToggleSlider;

  init {
    arg params;
    
    this.triggerDelayOne = KrNumberEditor.new(0.0);
    this.triggerDelayTwo = KrNumberEditor.new(0.0);
    
    //  these are the controls that will map to the parameters of the two
    //  delays and input sources
    this.synkopationControlOne = KrNumberEditor.new(0.0, ControlSpec(0.0, 1.0, \linear, (1.0/16.0)));
    this.synkopationControlTwo = KrNumberEditor.new(0.0, ControlSpec(0.0, 1.0, \linear, (1.0/16.0)));
    this.delayFactorControl = KrNumberEditor.new(1, ControlSpec(0, 2, \linear, (1.0 / 4.0)));
    this.delayFeedbackControl = KrNumberEditor.new(0.5, ControlSpec(0.0, 0.999999, \linear));

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
      feedbackCoefficient: this.delayFeedbackControl
    ));

    this.delayPatchTwo = FxPatch("cs.Synkopater.SynkopaterDelay", (
      numChan: 2,
      delaySecs: KrNumberEditor.new(0.0, ControlSpec(0.0, 8.0)),
      feedbackCoefficient: this.delayFeedbackControl
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
    var currentBeatsPerSecond = Tempo.tempo;

    this.delayPatchOne.delaySecs.value = (
      this.delayFactorControl.value() * (
        currentBeatsPerSecond + (
          currentBeatsPerSecond * this.synkopationControlOne.value()
        )
      )
    );
    this.delayPatchTwo.delaySecs.value = (
      this.delayFactorControl.value() * (
        currentBeatsPerSecond + (
          currentBeatsPerSecond * this.synkopationControlTwo.value()
        )
      )
    );

    this.triggerDelayOne.value = (
      currentBeatsPerSecond * this.synkopationControlOne.value()
    );

    this.triggerDelayTwo.value = (
      currentBeatsPerSecond * this.synkopationControlTwo.value()
    );
  }

  load_environment {
    var me = this,
      t = TempoClock.default;
    
    super.load_environment();
    
    this.synkopationControlOne.action = {
      arg val;
      me.handle_synkopation_control_changed();
    };

    this.synkopationControlTwo.action = {
      arg val;
      me.handle_synkopation_control_changed();
    };

    this.delayFactorControl.action = {
      arg val;
      me.handle_synkopation_control_changed();
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

  /**
   *  On the last quarter note of each bar, schedule notes for the start of
   *  the next bar.
   **/
  schedule_notes {
    var nextOne,
      nextThree,
      nextTwo,
      nextFour,
      t = TempoClock.default;

    nextOne = t.nextTimeOnGrid(t.beatsPerBar, 0);
    nextTwo = t.nextTimeOnGrid(t.beatsPerBar, 1);
    nextThree = t.nextTimeOnGrid(t.beatsPerBar, 2);
    nextFour = t.nextTimeOnGrid(t.beatsPerBar, 3);

    this.impulsePatchOne.trigger1(
      440,
      lat: this.triggerDelayOne.value + (t.beats2secs(nextOne) - t.seconds)
    );
    this.impulsePatchTwo.trigger1(
      880,
      lat: this.triggerDelayTwo.value + (t.beats2secs(nextTwo) - t.seconds)
    );
    this.impulsePatchOne.trigger1(
      440,
      lat: this.triggerDelayOne.value + (t.beats2secs(nextThree) - t.seconds)
    );
    this.impulsePatchTwo.trigger1(
      880,
      lat: this.triggerDelayTwo.value + (t.beats2secs(nextFour) - t.seconds)
    );

    if (this.playing, {
      ^t.beatsPerBar;
    }, {
      ^nil;
    });
  }

  on_play {
    var me = this,
      nextPickupNote,
      tempoClock = TempoClock.default;

    if (this.playing == false, {
      nextPickupNote = tempoClock.nextTimeOnGrid(
        tempoClock.beatsPerBar,
        tempoClock.beatsPerBar - 1
      );

      tempoClock.schedAbs(nextPickupNote, {
        me.schedule_notes();
      });
    });

    super.on_play();

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
      
      ArgNameLabel("delayFactor", layout, labelWidth);
      this.delayFactorControl.gui(layout);
      layout.startRow();
      
      ArgNameLabel("delayFeedback", layout, labelWidth);
      this.delayFeedbackControl.gui(layout);
      layout.startRow();

      ArgNameLabel("amp", layout, labelWidth);
      this.ampAndToggleSlider.gui(layout);
      layout.startRow();

    });
  }
  
  init_uc33_mappings {
    this.map_uc33_to_property(\knu1, \synkopationControlOne);
    this.map_uc33_to_property(\knm1, \synkopationControlTwo);
    this.map_uc33_to_property(\knl1, \delayFactorControl);
    this.map_uc33_to_property(\knu2, \delayFeedbackControl);
    this.map_uc33_to_property(\sl1, \ampAndToggleSlider);
  }
}
