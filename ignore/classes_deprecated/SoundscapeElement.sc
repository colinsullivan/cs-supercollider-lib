/**
 *  @file       SoundscapeElement.sc
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *              Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/

/**
 *  @class  Base class for all soundscape elements that are triggered with
 *  a particular frequency.
 **/
SoundscapeElement : Object {
  var <>outChannel,
    /**
     *  A unique symbol to use for this element.  Override in child
     *  classes.
     **/
    <>key,
    // reference to Soundscape instance
    <>soundscape,
    // a reference to the BufferManager instance
    <>bufManager,
    // instrument we are triggering
    <>instr,
    // transition on and off envelope duration
    <>transitionTime,
    // maximum time this sound will be played for
    <>onTimeMax,
    // minimum time this sound will be played for
    <>onTimeMin,
    // minimum duration between previous off and next on
    <>offTimeMin,
    // maximum duration between previous off and next on
    <>offTimeMax,
    // amount of reverb :)
    <>reverbLevel;

  init {
    arg args;

    this.key = args[\key];
    this.soundscape = args[\soundscape];
    this.bufManager = args[\bufManager];

    this.outChannel = MixerChannel.new(
      this.key,
      Server.default,
      2,
      2,
      1.0,
      outbus: this.soundscape.masterChannel
    );
    this.outChannel.guiUpdateTime = 0.05;
  }

  /**
   *  Called from `play` when we're preparing to play this element.  This 
   *  should be overridden in subclasses and return a `Patch` instance which
   *  will be played at the appropriate time.
   **/
  create_next_patch {
  
  }

  /**
   *  Called from soundscape when all buffers have been loaded.
   **/
  prepare_to_play {
  }

  /**
   *  Called when element should start playing.  The amount of time it will
   *  play for (plus `this.transitionTime`) is passed as an argument.
   *
   *  @param  onTime  The amount of time the voice will be on for is this
   *  argument plus `this.transitionTime`.
   **/
  element_on {
    arg onTime;

    // turn sound on
    this.instr.set(\gate, 1);
  }

  /**
   *  Called from soundscape when it starts up.
   **/
  play {
    var onTime,
      offTime;
   
    /**
     *  Main run loop.
     **/
    {

      while({ true }, {
        // prepare instrument (and maybe onTime / offTime constraints)
        this.instr = this.create_next_patch();
        this.outChannel.play(this.instr);
       
        // calculate how long to wait before playing 
        offTime = rrand(this.offTimeMin, this.offTimeMax);
        // calculate how long to play before stopping
        onTime = rrand(this.onTimeMin, this.onTimeMax) - (2.0 * this.transitionTime);

        // wait for the desired off time
        offTime.wait();
        
        this.element_on(onTime);

        // wait for transition time
        this.transitionTime.wait();
        
        // wait for on time
        onTime.wait();

        // turn sound off
        this.instr.set(\gate, 0);

        // off envelope time is same as on time
        this.transitionTime.wait();

        // stop patch (just for safety)
        this.instr.stop();
      
      });
    
    }.fork();
  
  }
}
