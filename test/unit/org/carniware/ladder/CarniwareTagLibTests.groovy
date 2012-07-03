package org.carniware.ladder



import grails.test.mixin.*
import org.junit.*
import java.math.RoundingMode
import com.carniware.ladder.CarniwareTagLib

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(CarniwareTagLib)
class CarniwareTagLibTests {

    void testRatingChangeBadge() {
        assert applyTemplate('<cw:ratingChangeBadge ratingChange="${rc}" />', [rc: new BigDecimal(5).setScale(0, RoundingMode.HALF_UP)]) == '<span class="badge badge-success"><i class="icon icon-arrow-up"></i> 5</span>'
        assert applyTemplate('<cw:ratingChangeBadge ratingChange="${rc}" />', [rc: new BigDecimal(-29).setScale(0, RoundingMode.HALF_UP)]) == '<span class="badge badge-important"><i class="icon icon-arrow-down"></i> 29</span>'
    }
}
