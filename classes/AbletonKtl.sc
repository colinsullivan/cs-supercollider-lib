/**
 *  @file       AbletonKtl.sc
 *
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *	@copyright	Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/

/**
 *  @class  AbletonKtl
 *  @desc   Easy interface for mapping MIDI from an Ableton track.
 **/
AbletonKtl : MIDIKtl {

  classvar <>verbose = true;

  *makeDefaults {

    var abletonTrackCCs,
      numCCs = 121,
      abletonChannelNum = 0;

    abletonTrackCCs = Dictionary.new(numCCs);

    numCCs.do({
      arg i;

      // the ccnumber in Ableton
      var ccNum = i + 1,
        ccKey = (
          abletonChannelNum.asString ++ "_" ++ ccNum.asString()
        ).asSymbol();

      abletonTrackCCs.put(ccKey, ccKey);
    });

    defaults.put(this, abletonTrackCCs);
  
  }

}
