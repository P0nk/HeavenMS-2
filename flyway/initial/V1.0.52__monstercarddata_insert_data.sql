INSERT INTO `monstercarddata`
    (`cardid`, `mobid`)
    (SELECT itemid, min(dropperid)
     FROM drop_data
     where itemid >= 2380000
       and itemid < 2390000
     group by itemid);