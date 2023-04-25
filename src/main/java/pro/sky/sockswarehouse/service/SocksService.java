package pro.sky.sockswarehouse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.sockswarehouse.dto.SocksDto;
import pro.sky.sockswarehouse.exception.NoNumberOfSocksException;
import pro.sky.sockswarehouse.exception.SocksNotFoundException;
import pro.sky.sockswarehouse.entity.Socks;
import pro.sky.sockswarehouse.operation.ComparisonOperator;
import pro.sky.sockswarehouse.repository.SocksRepository;

@Service
public class SocksService {

    private static final Logger logger = LoggerFactory.getLogger(SocksService.class);

    private final SocksRepository socksRepository;

    public SocksService(SocksRepository socksRepository) {
        this.socksRepository = socksRepository;
    }

    /**
     * Search for socks in the warehouse that match the specified parameters
     * @param   color               Socks color
     * @param   comparisonOperator  Comparison operator for the value of the amount of cotton in the composition of socks
     * @param   cottonPart          The percentage of cotton in the composition of socks
     * @return  The total number of socks in stock that meet the specified parameters
     */
    public String getSocks(String color, ComparisonOperator comparisonOperator, int cottonPart) {
        logger.info("The method was called to search for {} socks with a cotton content of {} {}",
                color, comparisonOperator, cottonPart);
        if (cottonPart < 0 || cottonPart > 100) {
            throw new IllegalArgumentException("Invalid cotton percentage value");
        }
        int quantity = 0;
        switch (comparisonOperator) {
            case lessThan -> quantity = socksRepository.getQuantitySocksByColorAndCottonPartLessThan(color, cottonPart)
                    .orElse(0);
            case equal -> quantity = socksRepository.getQuantitySocksByColorAndCottonPartEqual(color, cottonPart)
                    .orElse(0);
            case moreThan -> quantity = socksRepository.getQuantitySocksByColorAndCottonPartMoreThan(color, cottonPart)
                    .orElse(0);
        }
        return Integer.toString(quantity);
    }

    /**
     * Registration of receipt of socks to the warehouse
     * @param   socksDto    Dto containing data on color, cotton content and number of pairs of socks
     * @return  Registration of socks in the warehouse
     */
    public Socks incomeSocks(SocksDto socksDto) {
        logger.info("The sock receipt method was called");
        if (socksDto.getCottonPart() < 0 || socksDto.getCottonPart() > 100 || socksDto.getQuantity() < 0) {
            throw new IllegalArgumentException("Invalid cotton content or negative number of socks");
        }
        if (socksRepository.findSocksByColorAndCottonPart(socksDto.getColor(), socksDto.getCottonPart()).isEmpty()) {
            Socks socks = new Socks();
            socks.setColor(socksDto.getColor());
            socks.setCottonPart(socksDto.getCottonPart());
            socks.setQuantity(socksDto.getQuantity());
            return socksRepository.save(socks);
        } else {
            Socks socks = socksRepository
                    .findSocksByColorAndCottonPart(socksDto.getColor(), socksDto.getCottonPart())
                    .orElseThrow();
            socks.setQuantity(socks.getQuantity() + socksDto.getQuantity());
            return socksRepository.save(socks);
        }
    }

    /**
     * Registration of the issuance of socks from the warehouse
     * @param   socksDto    Dto containing data on color, cotton content and number of pairs of socks
     * @return  Registration of the issuance of socks from the warehouse
     */
    public Socks outcomeSocks(SocksDto socksDto) {
        logger.info("The sock issue method was called");
        if (socksDto.getCottonPart() < 0 || socksDto.getCottonPart() > 100 || socksDto.getQuantity() < 0) {
            throw new IllegalArgumentException("Invalid cotton content or negative number of socks");
        }
        Socks socksInStock = socksRepository
                .findSocksByColorAndCottonPart(socksDto.getColor(), socksDto.getCottonPart())
                .orElseThrow(SocksNotFoundException::new);
        if (socksInStock.getQuantity() < socksDto.getQuantity()) {
            throw new NoNumberOfSocksException();
        }
        socksInStock.setQuantity(socksInStock.getQuantity() - socksDto.getQuantity());
        return socksRepository.save(socksInStock);
    }
}
