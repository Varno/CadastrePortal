package bs.cadastre.dashboard.providers;

import com.vaadin.addon.jpacontainer.provider.BatchableLocalEntityProvider;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

public abstract class LocalEntityProviderBean<T> extends BatchableLocalEntityProvider<T> {

    protected final Log logger = LogFactory.getLog(getClass());
    @PersistenceContext
    private EntityManager em;

    protected LocalEntityProviderBean(Class<T> entityClass) {
        super(entityClass);
		setTransactionsHandledByProvider(false);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public T updateEntity(T entity) {
        if (logger.isDebugEnabled()) {
            logger.debug("Updating entity [" + entity + "]");
        }
        return super.updateEntity(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public T addEntity(T entity) {
        if (logger.isDebugEnabled()) {
            logger.debug("Adding entity [" + entity + "]");
        }
        return super.addEntity(entity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeEntity(Object entityId) {
        if (logger.isDebugEnabled()) {
            logger.debug("Removing entity identified by [" + entityId + "]");
        }
        super.removeEntity(entityId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateEntityProperty(Object entityId, String propertyName,
            Object propertyValue) throws IllegalArgumentException {
        if (logger.isDebugEnabled()) {
            logger.debug(
                    "Updating property [" + propertyName + "] of entity identified by [" + entityId + "] to [" + propertyValue + "]");
        }
        super.updateEntityProperty(entityId, propertyName, propertyValue);
    }

    @PostConstruct
    public void init() {
        Assert.notNull(em, "no entity manager has been specified");
        if (logger.isInfoEnabled()) {
            logger.info("Initializing entity provider bean");
        }
        setEntityManager(em);
        setEntitiesDetached(false);
    }
}
