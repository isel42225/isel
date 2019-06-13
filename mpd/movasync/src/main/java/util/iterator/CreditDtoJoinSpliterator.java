package util.iterator;

import movasync.dto.CreditDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CreditDtoJoinSpliterator extends Spliterators.AbstractSpliterator<CreditDto> {
    private final Spliterator<CreditDto> cast;
    private final Spliterator<CreditDto> crew;

    private CreditDto ct;
    private Object [] arr;
    private List<Integer> ids = new ArrayList<>();


    public CreditDtoJoinSpliterator(Spliterator<CreditDto> cast , Spliterator<CreditDto> crew ) {
        super( crew.estimateSize() + cast.estimateSize(), crew.characteristics() | cast.characteristics() );
        this.cast = cast;
        this.crew = crew;
        Stream<CreditDto> s = StreamSupport.stream(crew,false);

        //Spliterator<CreditDto> iter = s.spliterator();

         arr = s.toArray();

    }

    @Override
    public boolean tryAdvance(Consumer<? super CreditDto> action) {


            if (cast.tryAdvance(item -> ct = item ))
            {
                for(Object dto : arr ) {
                CreditDto cr = (CreditDto) dto;

                    //when is both
                    if (cr.getId() == ct.getId()) {
                        ids.add(cr.getId());
                        action.accept(new CreditDto(
                                cr.getId(),
                                ct.getCharacter(),
                                cr.getName(),
                                cr.getDepartment(),
                                cr.getJob())
                        );
                        return true;
                    }

                }

                //is only cast member
                action.accept(ct);
                ids.add(ct.getId());
                return true;

        }
        //get remaining crew members
        for(Object dto : arr )
        {
            CreditDto cr = (CreditDto) dto;

            if(!ids.contains(cr.getId()))
            {
                ids.add(cr.getId());
                action.accept(cr);
                return true;
            }
        }


        return false;
    }

}

